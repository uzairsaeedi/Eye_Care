package com.eyetest.eyecare.eyesighttest;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class LazyEyeActivity extends BaseActivity {

    private View movingDot;
    private TextView timerText;
    private Handler handler = new Handler();
    private Random random = new Random();
    private int screenWidth, screenHeight;
    private int dotSize = 60;
    private View ivBack;

    private Runnable moveRunnable = new Runnable() {
        @Override
        public void run() {
            int maxX = screenWidth - dotSize;
            int maxY = screenHeight - dotSize;

            int newX = random.nextInt(maxX);
            int newY = random.nextInt(maxY);

            movingDot.animate()
                    .x(newX)
                    .y(newY)
                    .setDuration(2000)
                    .start();

            handler.postDelayed(this, 2500);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lazy_eye);

        movingDot = findViewById(R.id.movingDot);
        timerText = findViewById(R.id.timerText);
        ivBack = findViewById(R.id.ivBack);

        ivBack.setOnClickListener(v -> finish());

        movingDot.post(() -> {
            screenWidth = ((View) movingDot.getParent()).getWidth();
            screenHeight = ((View) movingDot.getParent()).getHeight();
            handler.post(moveRunnable);
        });

        new CountDownTimer(45000, 1000) {
            public void onTick(long millisUntilFinished) {
                timerText.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                Intent intent = new Intent(LazyEyeActivity.this, Congratulations.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(moveRunnable);
    }
}
