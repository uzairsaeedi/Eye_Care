package com.eyecare;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.eyecare.Congratulations;
import com.eyecare.R;

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
                    .setDuration(2000) // slow movement
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

        // Back button
        ivBack.setOnClickListener(v -> finish());

        // Get screen size
        movingDot.post(() -> {
            screenWidth = ((View) movingDot.getParent()).getWidth();
            screenHeight = ((View) movingDot.getParent()).getHeight();
            handler.post(moveRunnable);
        });

        // Timer 45 seconds
        new CountDownTimer(45000, 1000) {
            public void onTick(long millisUntilFinished) {
                timerText.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                // Navigate to Congratulations screen
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
