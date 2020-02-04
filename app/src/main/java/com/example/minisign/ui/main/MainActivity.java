package com.example.minisign.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.minisign.R;
import com.example.minisign.ui.auth.loginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.main_toolbar)
     Toolbar toolbar;
    @BindView(R.id.main_pager)
     ViewPager viewPager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    private tabsAdapter tabsAdapter;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database ;
    DatabaseReference myRef ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        tabsAdapter=new tabsAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabsAdapter);

        tabLayout.setupWithViewPager(viewPager);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(currentUser!=null){
            setuserdata();
        }
    }

    private void setuserdata() {
        String currentUserId=mAuth.getCurrentUser().getUid();
        myRef.child("Users").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if((dataSnapshot.child("firstName").exists()))
                    Toast.makeText(MainActivity.this, dataSnapshot.child("firstName").getValue().toString(), Toast.LENGTH_SHORT).show();
                else sendUsertosettingsActiivity();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendUsertoLoginActiivity() {
        Intent loginintent=new Intent(MainActivity.this, loginActivity.class);
        startActivity(loginintent);
        finish();
    }
    private void sendUsertosettingsActiivity() {
        Intent loginintent=new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(loginintent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.option_menu,menu);
        return true;
    }

    private void RequestNewGroup() {
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this,R.style.AlertDialog);
        builder.setTitle("Enter group name");

        final EditText text=new EditText(MainActivity.this);
        text.setHint("\n e.g our MiniSign Team \n");
        builder.setView(text);
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String groupname=text.getText().toString();
                if(groupname.isEmpty())
                {
                    Toast.makeText(MainActivity.this,"Enter group name..",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    CreateNewGroup(groupname);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    private void CreateNewGroup(final String groupname) {
        database.getReference().child("Groups").child(groupname).setValue("")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                            Toast.makeText(MainActivity.this, groupname+"group is created successfully",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         super.onOptionsItemSelected(item);

         if(item.getItemId()==R.id.log_out_option){
             mAuth.signOut();
             SharedPreferences preferences= getSharedPreferences("lastlogin",MODE_PRIVATE);
             SharedPreferences.Editor editor=preferences.edit();
             editor.putString("email","");
             editor.putString("password","");
             editor.apply();
             sendUsertoLoginActiivity();
         }
         else if(item.getItemId()==R.id.settings_option){
             sendUsertosettingsActiivity();
         }
         else if (item.getItemId()==R.id.create_group)
         {
             RequestNewGroup();
         }
         else if(item.getItemId()==R.id.find_friend_option){

         }
         return true;
    }


}
