package com.example.minisign.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import butterknife.*;
import com.example.minisign.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {
    @BindView(R.id.name_editText)
     TextInputLayout name_textInputLayout;
    @BindView(R.id.status_editText)
     TextInputLayout status_textInputLayout;
    @BindView(R.id.phone_editText)
     TextInputLayout phone_textInputLayout;
    String name,status,phone;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database ;
    DatabaseReference myRef ;
    String currentUserId;
    public SettingsActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef=FirebaseDatabase.getInstance().getReference();
        setuserdata();
    }
    private void setuserdata() {
        currentUserId= Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        myRef.child("Users").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if((dataSnapshot.child("firstName").exists())){
                    String name=dataSnapshot.child("firstName").getValue().toString();
                    Objects.requireNonNull(name_textInputLayout.getEditText()).setText(name);
                }
                if((dataSnapshot.child("status").exists())){
                    String name=dataSnapshot.child("status").getValue().toString();
                    Objects.requireNonNull(status_textInputLayout.getEditText()).setText(name);
                }
                if((dataSnapshot.child("phone").exists())){
                    String name=dataSnapshot.child("phone").getValue().toString();
                    Objects.requireNonNull(phone_textInputLayout.getEditText()).setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void update_data(View view) {
        boolean valid = true;
         name = name_textInputLayout.getEditText().getText().toString().trim();
         status = status_textInputLayout.getEditText().getText().toString().trim();
        phone=phone_textInputLayout.getEditText().getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            name_textInputLayout.setError(getResources().getString(R.string.required));
            valid = false;
        } else {
            name_textInputLayout.setError(null);
        }
        if (TextUtils.isEmpty(status)) {
            status_textInputLayout.setError(getResources().getString(R.string.required));
            valid = false;
        } else {
            status_textInputLayout.setError(null);
        }
        if (TextUtils.isEmpty(phone)) {
            phone_textInputLayout.setError(getResources().getString(R.string.required));
            valid = false;
        } else {
            phone_textInputLayout.setError(null);
        }
        if(!valid)return;
        HashMap<String,String>profile_map=new HashMap<>();
        profile_map.put("firstName",name);
        profile_map.put("status",status);
        profile_map.put("phone",phone);
        myRef.child("Users").child(currentUserId).setValue(profile_map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(SettingsActivity.this, "profile updated", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(SettingsActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else Toast.makeText(SettingsActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}
