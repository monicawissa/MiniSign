package com.example.minisign.ui.auth;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.minisign.ui.main.MainActivity;
import com.example.minisign.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class Registration extends AppCompatActivity {
    EditText email,firstName,lastName,phone;
    TextInputLayout password,confirmPass;
    TextView Signup_tv;
    TextView forget_tv;

    Button reg_button;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();
        email=findViewById(R.id.editText_email);
        password=findViewById(R.id.editText_password);
        firstName=findViewById(R.id.editText_first);
        lastName=findViewById(R.id.editText_last);

        phone=findViewById(R.id.editText_phone);
        confirmPass=findViewById(R.id.editText_confirm);

        reg_button=findViewById(R.id.create_account);
    }
    public void Register(View view) {
        final String firstname = firstName.getText().toString().trim();
        final String lastname = lastName.getText().toString().trim();

        final String phonee = phone.getText().toString().trim();

        final String emaill = email.getText().toString().trim();
        final String passwordd = password.getEditText().getText().toString().trim();

        if (!validateForm()) {
            return;
        }
        mAuth.createUserWithEmailAndPassword(emaill, passwordd)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            user user = new user(firstname,lastname,emaill,phonee,passwordd);

                            FirebaseDatabase.getInstance().getReference().child("Users")
                                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Registration.this, R.string.Registration_success, Toast.LENGTH_LONG).show();
                                        Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(Registration.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
    private boolean validateForm() {
        boolean valid = true;
        final String firstname = firstName.getText().toString().trim();
        final String lastname = lastName.getText().toString().trim();

        final String phonee = phone.getText().toString().trim();

        String emaill = email.getText().toString().trim();
        String passwordd = password.getEditText().getText().toString().trim();
        String password_conf = confirmPass.getEditText().getText().toString().trim();

        if (TextUtils.isEmpty(emaill)) {
            email.setError(getResources().getString(R.string.required));
            email.requestFocus();
            valid = false;
        } else {
            email.setError(null);
        }
        if (TextUtils.isEmpty(firstname)) {
            firstName.setError(getResources().getString(R.string.required));
            firstName.requestFocus();
            valid = false;
        } else {
            firstName.setError(null);
        }
        if (TextUtils.isEmpty(lastname)) {
            lastName.setError(getResources().getString(R.string.required));
            lastName.requestFocus();
            valid = false;
        } else {
            lastName.setError(null);
        }
        if (TextUtils.isEmpty(phonee)||(phonee.length()!=10)) {
            phone.setError(getResources().getString(R.string.required_10_digit));
            phone.requestFocus();
            valid = false;
        } else {
            phone.setError(null);
        }
        if (TextUtils.isEmpty(passwordd)||passwordd.length()<6) {
            password.setError(getResources().getString(R.string.required));
            password.requestFocus();

            valid = false;
        } else {
            password.setError(null);
        }
        if (TextUtils.isEmpty(password_conf)) {
            confirmPass.setError(getResources().getString(R.string.required));
            confirmPass.requestFocus();

            valid = false;
        } else {
            confirmPass.setError(null);
        }
        if (!password_conf.equals(passwordd)) {
            confirmPass.setError(getResources().getString(R.string.confirm_error));
            confirmPass.requestFocus();

            valid = false;
        } else {
            confirmPass.setError(null);
        }

        return valid;
    }


}