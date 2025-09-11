package com.eyetest.eyecare.eyesighttest;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.progressindicator.CircularProgressIndicator;

public class KaleidoscopeActivity extends BaseActivity {

    private TextView timerText;
    private ImageView ivBack, ivKaleidoscope;
    private int totalTime = 30;
    CircularProgressIndicator circleProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kaleidoscope);

        ivKaleidoscope = findViewById(R.id.ivKaleidoscope);

        timerText = findViewById(R.id.timerText);
        ivBack = findViewById(R.id.ivBack);
        circleProgress = findViewById(R.id.circleProgress);

        circleProgress.setMax(totalTime);
        circleProgress.setProgress(totalTime);

        ivBack.setOnClickListener(v -> finish());

        startTimer();
        startKaleidoscopeAnimation();

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
                Intent intent = new Intent(KaleidoscopeActivity.this, Congratulations.class);
                startActivity(intent);
            }
        }.start();
    }

    private void startKaleidoscopeAnimation() {
        ObjectAnimator rotate = ObjectAnimator.ofFloat(ivKaleidoscope, "rotation", 0f, 360f);
        rotate.setDuration(12000); // 12 seconds for one full rotation
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setRepeatCount(ObjectAnimator.INFINITE);

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(ivKaleidoscope, "scaleX", 0.98f, 1.06f);
        scaleX.setDuration(2000);
        scaleX.setRepeatCount(ObjectAnimator.INFINITE);
        scaleX.setRepeatMode(ObjectAnimator.REVERSE);

        ObjectAnimator scaleY = ObjectAnimator.ofFloat(ivKaleidoscope, "scaleY", 0.98f, 1.06f);
        scaleY.setDuration(2000);
        scaleY.setRepeatCount(ObjectAnimator.INFINITE);
        scaleY.setRepeatMode(ObjectAnimator.REVERSE);

        ObjectAnimator translateY = ObjectAnimator.ofFloat(ivKaleidoscope, "translationY", 0f, -18f);
        translateY.setDuration(3000);
        translateY.setRepeatCount(ObjectAnimator.INFINITE);
        translateY.setRepeatMode(ObjectAnimator.REVERSE);
        translateY.setInterpolator(new LinearInterpolator());

        AnimatorSet set = new AnimatorSet();
        set.playTogether(rotate, scaleX, scaleY, translateY);
        set.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}