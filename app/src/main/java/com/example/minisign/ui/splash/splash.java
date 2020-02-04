package com.example.minisign.ui.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.minisign.ui.auth.lastlogin;
import com.example.minisign.ui.main.MainActivity;
import com.example.minisign.R;
import com.example.minisign.ui.auth.loginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;


public class splash extends AppCompatActivity {
    //Splash Screen Timer
    private static final int SPLASH_SCREEN_TIMER = 2000;
    private FirebaseAuth mAuth;
    private FirebaseUser currentuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // Initialize Firebase Auth
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(SPLASH_SCREEN_TIMER);
                    SharedPreferences preferences= getSharedPreferences("lastlogin",MODE_PRIVATE);
                    String em=preferences.getString("email",""),
                            password =preferences.getString("password","");
                    if(em.isEmpty()) {
                        Intent intent = (currentuser == null) ?
                                new Intent(splash.this, loginActivity.class) :
                                new Intent(splash.this, MainActivity.class);

                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        final String emaill = em;
                        final String passwordd = password;

                        FirebaseAuth.getInstance().signInWithEmailAndPassword(emaill, passwordd)
                                .addOnCompleteListener(splash.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(splash.this, "you are not registered", Toast.LENGTH_SHORT).show();
                                            try {
                                                throw Objects.requireNonNull(task.getException());
                                            }
                                            catch(Exception e) {
                                                Log.e("taggg", e.getMessage());
                                            }

                                        } else {
                                            startActivity(new Intent(splash.this, MainActivity.class));
                                            finish();
                                        }
                                    }
                                });
                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                super.run();
            }
        };
        thread.start();

    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}