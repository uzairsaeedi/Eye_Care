package com.eyetest.eyecare.eyesighttest;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class HyperopiaPrevention extends BaseActivity {

    private View dot, dotSmall1, dotSmall2;
    private TextView timerText;
    private ImageView ivBack;
    private ValueAnimator waveAnimator;
    private CountDownTimer countDownTimer;

    private static final int EXERCISE_TIME = 30000;
    private final Random rnd = new Random();

    private int rootWidth = 0;
    private int rootHeight = 0;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable smallDot1Runnable = new Runnable() {
        @Override
        public void run() {
            moveSmallDotToRandom(dotSmall1);
            handler.postDelayed(this, 600 + rnd.nextInt(800));
        }
    };

    private final Runnable smallDot2Runnable = new Runnable() {
        @Override
        public void run() {
            moveSmallDotToRandom(dotSmall2);
            handler.postDelayed(this, 700 + rnd.nextInt(900));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hyperopia_prevention);

        dot = findViewById(R.id.dot);
        dotSmall1 = findViewById(R.id.dotSmall1);
        dotSmall2 = findViewById(R.id.dotSmall2);
        timerText = findViewById(R.id.timerText);
        ivBack = findViewById(R.id.ivBack);

        ivBack.setOnClickListener(v -> finish());

        final View root = findViewById(android.R.id.content);
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override public void onGlobalLayout() {
                root.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                rootWidth = root.getWidth();
                rootHeight = root.getHeight();

                placeSmallDotInitial(dotSmall1);
                placeSmallDotInitial(dotSmall2);

                startWaveAnimation();            // main dot (big) movement with larger amplitude
                startSmallDotsRandomMovement();  // independent random roaming
            }
        });
        startTimer();
    }


    private void startWaveAnimation() {
        waveAnimator = ValueAnimator.ofFloat(0, (float) (4 * Math.PI)); // 2 cycles
        waveAnimator.setDuration(6000);
        waveAnimator.setInterpolator(new LinearInterpolator());
        waveAnimator.setRepeatCount(ValueAnimator.INFINITE);

        waveAnimator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();

            float amplitude = 150f;
            float wavelength = 350f;

            float x = (value / (2 * (float) Math.PI)) * wavelength;

            float y = (float) Math.sin(value) * amplitude;

            dot.setTranslationX(x);
            dot.setTranslationY(y);
        });

        waveAnimator.start();
    }

    private void startSmallDotsRandomMovement() {
        handler.postDelayed(smallDot1Runnable, 150);
        handler.postDelayed(smallDot2Runnable, 350);
    }

    private void moveSmallDotToRandom(View smallDot) {
        if (rootWidth <= 0 || rootHeight <= 0) return;

        final int minDistanceDp = 140; // minimum distance from main dot (so they don't hang near it)
        final int minDistancePx = dpToPx(minDistanceDp);

        int attempts = 0;
        int newX, newY;
        do {
            // safe bounds to avoid toolbar and bottom timer area
            int marginTop = dpToPx( (int)(56 + 8) ); // toolbar + little offset
            int marginBottom = dpToPx(120);
            int marginSides = dpToPx(12);

            int safeLeft = marginSides;
            int safeRight = Math.max(0, rootWidth - smallDot.getWidth() - marginSides);
            int safeTop = marginTop;
            int safeBottom = Math.max(0, rootHeight - smallDot.getHeight() - marginBottom);

            if (safeRight <= safeLeft) safeRight = safeLeft;
            if (safeBottom <= safeTop) safeBottom = safeTop;

            newX = safeLeft + rnd.nextInt(safeRight - safeLeft + 1);
            newY = safeTop + rnd.nextInt(safeBottom - safeTop + 1);

            attempts++;
            // try up to some attempts to find location away from main dot
        } while (distanceFromMainDotCenter(newX, newY, smallDot) < minDistancePx && attempts < 12);

        // animate to the new location
        long dur = 500 + rnd.nextInt(900); // 500-1400 ms
        smallDot.animate()
                .x(newX)
                .y(newY)
                .setDuration(dur)
                .setInterpolator(new LinearInterpolator())
                .start();
    }

    private void placeSmallDotInitial(View smallDot) {
        if (rootWidth <= 0 || rootHeight <= 0) return;

        final int minDistancePx = dpToPx(140);
        int attempts = 0;
        int newX, newY;
        do {
            int marginTop = dpToPx( (int)(56 + 8) );
            int marginBottom = dpToPx(120);
            int marginSides = dpToPx(12);

            int safeLeft = marginSides;
            int safeRight = Math.max(0, rootWidth - smallDot.getWidth() - marginSides);
            int safeTop = marginTop;
            int safeBottom = Math.max(0, rootHeight - smallDot.getHeight() - marginBottom);

            if (safeRight <= safeLeft) safeRight = safeLeft;
            if (safeBottom <= safeTop) safeBottom = safeTop;

            newX = safeLeft + rnd.nextInt(safeRight - safeLeft + 1);
            newY = safeTop + rnd.nextInt(safeBottom - safeTop + 1);
            attempts++;
        } while (distanceFromMainDotCenter(newX, newY, smallDot) < minDistancePx && attempts < 12);

        smallDot.setX(newX);
        smallDot.setY(newY);
    }

    private float distanceFromMainDotCenter(int candidateLeft, int candidateTop, View smallDot) {
        float mainCenterX = dot.getX() + dot.getWidth() * 0.5f;
        float mainCenterY = dot.getY() + dot.getHeight() * 0.5f;

        float candCenterX = candidateLeft + smallDot.getWidth() * 0.5f;
        float candCenterY = candidateTop + smallDot.getHeight() * 0.5f;

        float dx = candCenterX - mainCenterX;
        float dy = candCenterY - mainCenterY;
        return (float) Math.hypot(dx, dy);
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(EXERCISE_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerText.setText("" + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                stopExercise();
                Intent intent = new Intent(HyperopiaPrevention.this, Congratulations.class);
                startActivity(intent);
                finish();
            }
        };
        countDownTimer.start();
    }

    private void stopExercise() {
        if (waveAnimator != null && waveAnimator.isRunning()) {
            waveAnimator.cancel();
            waveAnimator = null;
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        handler.removeCallbacks(smallDot1Runnable);
        handler.removeCallbacks(smallDot2Runnable);

        if (dotSmall1 != null) dotSmall1.animate().cancel();
        if (dotSmall2 != null) dotSmall2.animate().cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopExercise();
    }
    private int dpToPx(int dp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics()));
    }
}