package com.example.minisign.ui.splash;

import android.content.Intent;
import android.os.Bundle;

import com.example.minisign.ui.main.MainActivity;
import com.example.minisign.R;
import com.example.minisign.ui.auth.loginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class splash extends AppCompatActivity {
    //Splash Screen Timer
    private static final int SPLASH_SCREEN_TIMER = 1000;
    private FirebaseAuth mAuth;
    private FirebaseUser currentuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // Initialize Firebase Auth
        Thread thread=new Thread(){
            @Override
            public void run() {
                try {
                    sleep(SPLASH_SCREEN_TIMER);
                    Intent intent=(currentuser==null)?
                        new Intent(splash.this, loginActivity.class):
                        new Intent(splash.this, MainActivity.class);

                    startActivity(intent);
                    finish();
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
