package com.eyetest.eyecare.eyesighttest;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.progressindicator.CircularProgressIndicator;

public class FocusShift extends BaseActivity {

    private TextView centerCircle, timerText;
    private String letters = "AXYV";
    private int letterIndex = 0;
    private int totalTime = 30;
    private ImageView ivBack;

    CircularProgressIndicator circleProgress;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus_shift);

        centerCircle = findViewById(R.id.centerCircle);
        timerText = findViewById(R.id.timerText);
        ivBack = findViewById(R.id.ivBack);
        circleProgress = findViewById(R.id.circleProgress);

       circleProgress.setMax(totalTime);
       circleProgress.setProgress(totalTime);

       ivBack.setOnClickListener(v -> finish());

        startTimer();
        startAnimation();

    }

    private void startTimer() {
        new CountDownTimer(totalTime * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsLeft = (int) (millisUntilFinished / 1000);
                timerText.setText(String.valueOf(secondsLeft));

                circleProgress.setProgressCompat(secondsLeft, true);
            }

            @Override
            public void onFinish() {
                timerText.setText("0");
                circleProgress.setProgress(0);
                Intent intent = new Intent(FocusShift.this, Congratulations.class);
                startActivity(intent);
            }
        }.start();
    }

    private void startAnimation() {
        final ScaleAnimation scaleAnim = new ScaleAnimation(
                0.5f, 1.2f, // fromX, toX
                0.5f, 1.2f, // fromY, toY
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnim.setDuration(1500);
        scaleAnim.setRepeatCount(Animation.INFINITE);
        scaleAnim.setRepeatMode(Animation.REVERSE);

        scaleAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) { }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Change Letter after every cycle
                letterIndex = (letterIndex + 1) % letters.length();
                centerCircle.setText("" + letters.charAt(letterIndex));
            }

            @Override
            public void onAnimationStart(Animation animation) { }
        });

        centerCircle.startAnimation(scaleAnim);
    }
}