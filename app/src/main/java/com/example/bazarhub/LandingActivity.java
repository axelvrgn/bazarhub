package com.example.bazarhub;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bazarhub.Auth.LoginActivity;

public class LandingActivity extends AppCompatActivity {

    private static final long ANIMATION_DURATION = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        ImageView logoImageView = findViewById(R.id.logoImageView);
        Log.d("ImageViewDebug", "ImageView Visibility: " + logoImageView.getVisibility());

        logoImageView.setVisibility(View.VISIBLE);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale_animation);

        new Handler().postDelayed(() -> {
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    startActivity(new Intent(LandingActivity.this, LoginActivity.class));
                    finish();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });

            logoImageView.startAnimation(animation);
        }, ANIMATION_DURATION);
    }
}
