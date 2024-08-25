package com.example.myapplication.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.myapplication.R;


public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH_DURATION = 5000; // 5 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        loadGif();
        hideActionBar();
        scheduleTransitionToMainActivity();
    }


    private void hideActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    private void loadGif() {
        ImageView imageView = findViewById(R.id.splashLoading);
        Glide.with(this)
                .asGif()
                .load(R.drawable.loading)
                .into(imageView);
    }


    private void scheduleTransitionToMainActivity() {
        new Handler().postDelayed(this::navigateToMainActivity, SPLASH_DURATION);
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
