package com.eyetest.eyecare.eyesighttest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class TrackAnimalActivity extends BaseActivity {

    private View root;
    private ImageView ivRat;
    private TextView tvScore, tvTimer;
    private View redOverlay;

    private final Random random = new Random();

    private int score = 0;
    private final int PASS_THRESHOLD = 5;
    private final long DURATION_MS = 30_000;
    private int rootWidth = 0, rootHeight = 0;
    private CountDownTimer timer;
    private Vibrator vibrator;

    private AnimatorSet currentAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_animal);

        root = findViewById(R.id.root);
        ivRat = findViewById(R.id.ivRat);
        tvScore = findViewById(R.id.tvScore);
        tvTimer = findViewById(R.id.tvTimer);
        redOverlay = findViewById(R.id.redOverlay);

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override public void onGlobalLayout() {
                root.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                rootWidth = root.getWidth();
                rootHeight = root.getHeight();
                startTimer();
                startNextMovement();
            }
        });

        ivRat.setOnClickListener(v -> {
            onRatClicked();
        });

        root.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                float x = event.getX();
                float y = event.getY();

                int[] loc = new int[2];
                ivRat.getLocationOnScreen(loc);
                int[] rootLoc = new int[2];
                root.getLocationOnScreen(rootLoc);

                float ratLeft = ivRat.getX();
                float ratTop = ivRat.getY();
                float ratRight = ratLeft + ivRat.getWidth();
                float ratBottom = ratTop + ivRat.getHeight();

                if (!(x >= ratLeft && x <= ratRight && y >= ratTop && y <= ratBottom)) {
                    onMissClick();
                }
            }
            return true;
        });

        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
    }

    private void onRatClicked() {
        if (vibrator != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(70, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(70);
            }
        }

        ivRat.animate().scaleX(0.85f).scaleY(0.85f).setDuration(80)
                .withEndAction(() -> ivRat.animate().scaleX(1f).scaleY(1f).setDuration(120).start()).start();

        score++;
        tvScore.setText(String.valueOf(score));

        stopCurrentMovement();
        ivRat.postDelayed(this::startNextMovement, 200);
    }

    private void onMissClick() {
        redOverlay.setAlpha(0f);
        redOverlay.setVisibility(View.VISIBLE);
        redOverlay.animate().alpha(1f).setDuration(80).withEndAction(() ->
                redOverlay.animate().alpha(0f).setDuration(250).withEndAction(() ->
                        redOverlay.setVisibility(View.GONE)).start()).start();

        stopCurrentMovement();
        ivRat.postDelayed(this::startNextMovement, 200);
    }

    private void stopCurrentMovement() {
        if (currentAnimator != null) {
            currentAnimator.cancel();
            currentAnimator = null;
        }
    }

    private void startNextMovement() {
        if (rootWidth == 0 || rootHeight == 0) return;

        int pattern = random.nextInt(5);

        int margin = dpToPx(24);
        int ratW = ivRat.getWidth();
        int ratH = ivRat.getHeight();

        long duration = 2400 + random.nextInt(700);

        ObjectAnimator animX = null;
        ObjectAnimator animY = null;
        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.setDuration(duration);

        switch (pattern) {
            case 0:
            {
                int x = margin + random.nextInt(Math.max(1, rootWidth - ratW - 2*margin));
                float startY = -ratH - 20;
                float endY = rootHeight + 20;
                ivRat.setX(x);
                ivRat.setY(startY);
                animY = ObjectAnimator.ofFloat(ivRat, "y", startY, endY);
                set.play(animY);
                break;
            }

            case 1:
            {
                int y = margin + random.nextInt(Math.max(1, rootHeight - ratH - 2*margin));
                float startX = -ratW - 20;
                float endX = rootWidth + 20;
                ivRat.setY(y);
                ivRat.setX(startX);
                animX = ObjectAnimator.ofFloat(ivRat, "x", startX, endX);
                set.play(animX);
                break;
            }

            case 2:
            {
                float startX = -ratW - 20;
                float startY = -ratH - 20;
                float endX = rootWidth + 20;
                float endY = rootHeight + 20;
                ivRat.setX(startX);
                ivRat.setY(startY);
                animX = ObjectAnimator.ofFloat(ivRat, "x", startX, endX);
                animY = ObjectAnimator.ofFloat(ivRat, "y", startY, endY);
                set.playTogether(animX, animY);
                break;
            }

            case 3:
            {
                float startX = rootWidth + 20;
                float startY = -ratH - 20;
                float endX = -ratW - 20;
                float endY = rootHeight + 20;
                ivRat.setX(startX);
                ivRat.setY(startY);
                animX = ObjectAnimator.ofFloat(ivRat, "x", startX, endX);
                animY = ObjectAnimator.ofFloat(ivRat, "y", startY, endY);
                set.playTogether(animX, animY);
                break;
            }

            default:
            {
                int edge = random.nextInt(4);
                if (edge == 0) {
                    int x = margin + random.nextInt(Math.max(1, rootWidth - ratW - 2*margin));
                    float startY = -ratH - 20;
                    float endY = rootHeight + 20;
                    ivRat.setX(x); ivRat.setY(startY);
                    animY = ObjectAnimator.ofFloat(ivRat, "y", startY, endY);
                    set.play(animY);
                } else if (edge == 1) {
                    int x = margin + random.nextInt(Math.max(1, rootWidth - ratW - 2*margin));
                    float startY = rootHeight + 20;
                    float endY = -ratH - 20;
                    ivRat.setX(x); ivRat.setY(startY);
                    animY = ObjectAnimator.ofFloat(ivRat, "y", startY, endY);
                    set.play(animY);
                } else if (edge == 2) {
                    int y = margin + random.nextInt(Math.max(1, rootHeight - ratH - 2*margin));
                    float startX = -ratW - 20;
                    float endX = rootWidth + 20;
                    ivRat.setY(y); ivRat.setX(startX);
                    animX = ObjectAnimator.ofFloat(ivRat, "x", startX, endX);
                    set.play(animX);
                } else {
                    int y = margin + random.nextInt(Math.max(1, rootHeight - ratH - 2*margin));
                    float startX = rootWidth + 20;
                    float endX = -ratW - 20;
                    ivRat.setY(y); ivRat.setX(startX);
                    animX = ObjectAnimator.ofFloat(ivRat, "x", startX, endX);
                    set.play(animX);
                }
                break;
            }
        }

        set.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(Animator animation) {
                currentAnimator = null;
                ivRat.postDelayed(TrackAnimalActivity.this::startNextMovement, 180);
            }
            @Override public void onAnimationCancel(Animator animation) {
            }
        });

        long extra = random.nextInt(900);
        set.setDuration(Math.max(600, set.getDuration() + extra));

        currentAnimator = set;
        set.start();
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private void startTimer() {
        timer = new CountDownTimer(DURATION_MS, 1000) {
            @Override public void onTick(long millisUntilFinished) {
                int secs = (int) (millisUntilFinished / 1000);
                tvTimer.setText(String.valueOf(secs));
            }

            @Override public void onFinish() {
                tvTimer.setText("0");
                evaluateResult();
            }
        }.start();
    }

    private void evaluateResult() {
        boolean passed = score >= PASS_THRESHOLD;
        showResultDialog(passed);
    }

    private void showResultDialog(boolean passed) {
        String title = passed ? "Level Passed" : "Level Failed";
        String message = passed ? "Great job! You scored " + score + "." : "Try again. You scored " + score + ".";

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(passed ? "Next Level" : "Retry", (dialog, which) -> {
                    if (passed) finish();
                    else restartLevel();
                })
                .setNegativeButton("Home", (dialog, which) -> {
                    Intent intent = new Intent(TrackAnimalActivity.this, MainActivity.class);
                    startActivity(intent);
                })
                .show();
    }

    private void restartLevel() {
        stopCurrentMovement();
        score = 0;
        tvScore.setText(String.valueOf(score));
        if (timer != null) timer.cancel();
        startTimer();
        startNextMovement();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) timer.cancel();
        stopCurrentMovement();
    }
}
