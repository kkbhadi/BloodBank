package com.bloodbankapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bloodbankapp.R;
import com.bloodbankapp.firebase.FirebaseRepository;
import com.bloodbankapp.pref.SharedPrefUtil;
import com.bloodbankapp.ui.LoginActivity;

public class SplashActivity extends AppCompatActivity {
    ImageView imageView;
    TextView textView;
    Handler handler;
    FirebaseRepository repository;
    SharedPrefUtil sharedPrefUtil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        textView = (TextView) findViewById(R.id.tvWelcomeSplash);
        imageView = (ImageView) findViewById(R.id.imgDrop);

        // Get Repo
        repository = new FirebaseRepository();

        // get Pref
        sharedPrefUtil = new SharedPrefUtil(this);

        bounceAnim();
        ZoomTextAnim();
        nextScreen();
    }

    public void bounceAnim() {
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.anim_drop_down);
        imageView.startAnimation(animation1);
        imageView.setVisibility(View.VISIBLE);
    }

    public void ZoomTextAnim() {
        Animation zoomAnim = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.anim_slide_up);
        textView.startAnimation(zoomAnim);
        textView.setVisibility(View.VISIBLE);
    }

    public void nextScreen() {

        final Intent intentGo;
        if (sharedPrefUtil.getId().isEmpty()) {
            intentGo = new Intent(SplashActivity.this, LoginActivity.class);
        } else {
            intentGo = new Intent(SplashActivity.this, HomeActivity.class);
        }
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intentGo);
                finish();
            }
        }, 3000);
    }
}