package com.example.minisign.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.minisign.ui.main.MainActivity;
import com.example.minisign.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class loginActivity extends AppCompatActivity {
    EditText email;
    TextInputLayout password;
    TextView Signup_tv;
    TextView forget_tv;
    private FirebaseAuth mAuth;
    Button login_button;
    Button google_button;
    Button face_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llogin);
        mAuth = FirebaseAuth.getInstance();
        email=findViewById(R.id.login_email_editText);

        password=findViewById(R.id.login_password_editText);

        Signup_tv=findViewById(R.id.login_new_account_text_view);
        forget_tv=findViewById(R.id.login_forget_password_text_view);

        login_button=findViewById(R.id.login_button);

        google_button=findViewById(R.id.login_google_button);
        face_button=findViewById(R.id.login_facebook_button);
    }
    public void sign_up(View view) {
        Intent intent=(new Intent(this, Registration.class));
        startActivity(intent);
        finish();

    }
    public void login(View view) {
        String emaill = email.getText().toString().trim();
        String passwordd = password.getEditText().getText().toString().trim();

        if (!validateForm()) {
            return;
        }
        //create user
        mAuth.signInWithEmailAndPassword(emaill, passwordd)
                .addOnCompleteListener(loginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            try {
                                throw task.getException();
                            } catch(FirebaseAuthInvalidCredentialsException e) {
                                email.setError(getString(R.string.error_invalid_email));
                                email.requestFocus();
                            } catch(FirebaseAuthUserCollisionException e) {
                                email.setError(getString(R.string.error_user_exists));
                                email.requestFocus();
                            } catch(Exception e) {
                                Log.e("taggg", e.getMessage());
                            }

                        } else {
                            startActivity(new Intent(loginActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                });

    }

    public void forget_your_password(View view) {
    }
    public void login_facebook(View view) {
    }
    public void login_google(View view) {
    }
    private boolean validateForm() {
        boolean valid = true;
        String emaill = email.getText().toString().trim();
        String passwordd = password.getEditText().getText().toString().trim();

        if (TextUtils.isEmpty(emaill)) {
            email.setError(getResources().getString(R.string.required));
            valid = false;
        } else {
            email.setError(null);
        }

        if (TextUtils.isEmpty(passwordd)) {
            password.setError(getResources().getString(R.string.required));
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }

}
