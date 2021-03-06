package com.barathi.user.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.barathi.user.R;
import com.barathi.user.SessionManager;
import com.barathi.user.retrofit.Const;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;

public class SpleshActivity extends AppCompatActivity {

    private static final long SPLASH_SCREEN_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*getWindow().setFlags(FLAG_FULLSCREEN,
                FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_splesh);


        Animation animation;
        animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.spleshanim);
        findViewById(R.id.splesh).startAnimation(animation);


        SessionManager sessionManager = new SessionManager(this);
        FirebaseMessaging.getInstance().subscribeToTopic("Veggi")
                .addOnCompleteListener(task -> {
                    Log.d("TAG", "onComplete: fcm");
                    if(!task.isSuccessful()) {
                        Log.d("TAG", "onComplete:failll " + task.toString());
                    } else {
                        Log.d("TAG", "success");
                    }
                });
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if(!task.isSuccessful()) {
                        Log.w("notification", "getInstanceId failed", task.getException());
                        return;
                    }
                    // Get new Instance ID token
                    if(task.getResult() != null) {
                        String token = task.getResult().getToken();
                        sessionManager.saveStringValue(Const.NOTIFICATION_TOKEN, token);
                        Log.d("notification", token);
                    }
                    // Log and toast
                });


        new Handler().postDelayed(() -> {
            Intent i = new Intent(SpleshActivity.this,
                    MainActivity.class);


            startActivity(i);
            //invoke the SecondActivity.

            finish();
            //the current activity will get finished.
        }, SPLASH_SCREEN_TIME_OUT);
    }
}