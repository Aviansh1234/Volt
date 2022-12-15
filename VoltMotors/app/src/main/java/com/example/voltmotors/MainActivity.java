package com.example.voltmotors;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(auth.getCurrentUser()==null){
                    startActivity(new Intent(MainActivity.this, SignUpSignInActivity.class));
                }
                else{
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                }
                finish();
            }
        }, 2000);
    }
}