package com.eyetest.eyecare.eyesighttest;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import java.util.Random;

public class ColorMatchActivity extends BaseActivity {

    private View root, viewColorCircle, redOverlay;
    private TextView tvScore, tvTimer;
    private View[] btnColors = new View[5];

    private final Handler handler = new Handler();
    private final Random rnd = new Random();

    private final int TOTAL_ROUNDS = 10;
    private final int ROUND_DURATION_MS = 10_000;
    private final int PASS_THRESHOLD = 6;

    private final int[] COLORS = new int[] {
            Color.parseColor("#A8D8FF"),
            Color.parseColor("#CBB5FF"),
            Color.parseColor("#F1C7FF"),
            Color.parseColor("#FFCCE6"),
            Color.parseColor("#FFC6C6")
    };

    private int currentRound = 0;
    private int currentColorIndex = 0;
    private int score = 0;

    private CountDownTimer roundTimer;
    private ObjectAnimator scaleAnimX, scaleAnimY;

    private boolean roundActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_match);

        root = findViewById(R.id.root);
        viewColorCircle = findViewById(R.id.viewColorCircle);
        redOverlay = findViewById(R.id.redOverlay);
        tvScore = findViewById(R.id.tvScore);
        tvTimer = findViewById(R.id.tvTimer);

        btnColors[0] = findViewById(R.id.btnColor0);
        btnColors[1] = findViewById(R.id.btnColor1);
        btnColors[2] = findViewById(R.id.btnColor2);
        btnColors[3] = findViewById(R.id.btnColor3);
        btnColors[4] = findViewById(R.id.btnColor4);

        findViewById(R.id.ivBack).setOnClickListener(v -> finish());

        // set bottom colors to the fixed palette and clicks
        for (int i = 0; i < btnColors.length; i++) {
            safeSetViewColor(btnColors[i], COLORS[i], false);
            final int idx = i;
            btnColors[i].setOnClickListener(v -> onColorButtonClick(idx));
        }

        // Ensure circle has a drawable we can update
        safeSetViewColor(viewColorCircle, COLORS[0], true);

        // Start game after layout pass to ensure view measured
        handler.postDelayed(this::startGame, 120);
    }

    private void startGame() {
        currentRound = 0;
        score = 0;
        updateScore();
        nextRound();
    }

    private void nextRound() {
        if (currentRound >= TOTAL_ROUNDS) {
            endGame();
            return;
        }
        currentRound++;
        currentColorIndex = rnd.nextInt(COLORS.length);

        // set big circle color (safely)
        safeSetViewColor(viewColorCircle, COLORS[currentColorIndex], true);

        // cancel previous animators
        if (scaleAnimX != null) scaleAnimX.cancel();
        if (scaleAnimY != null) scaleAnimY.cancel();

        viewColorCircle.setScaleX(0.6f);
        viewColorCircle.setScaleY(0.6f);

        scaleAnimX = ObjectAnimator.ofFloat(viewColorCircle, "scaleX", 0.6f, 1.5f);
        scaleAnimY = ObjectAnimator.ofFloat(viewColorCircle, "scaleY", 0.6f, 1.5f);
        scaleAnimX.setDuration(ROUND_DURATION_MS);
        scaleAnimY.setDuration(ROUND_DURATION_MS);
        scaleAnimX.setInterpolator(new LinearInterpolator());
        scaleAnimY.setInterpolator(new LinearInterpolator());
        scaleAnimX.start();
        scaleAnimY.start();

        if (roundTimer != null) roundTimer.cancel();
        roundTimer = new CountDownTimer(ROUND_DURATION_MS, 1000) {
            @Override public void onTick(long millisUntilFinished) {
                int secs = (int) ((millisUntilFinished + 999) / 1000);
                tvTimer.setText(String.valueOf(secs));
            }
            @Override public void onFinish() {
                tvTimer.setText("0");
                roundActive = false;
                handler.postDelayed(() -> nextRound(), 300);
            }
        }.start();

        roundActive = true;
    }

    private void onColorButtonClick(int clickedIndex) {
        if (!roundActive) return;
        roundActive = false;

        if (roundTimer != null) {
            roundTimer.cancel();
            roundTimer = null;
        }
        if (scaleAnimX != null) scaleAnimX.cancel();
        if (scaleAnimY != null) scaleAnimY.cancel();

        if (clickedIndex == currentColorIndex) {
            // correct
            score++;
            updateScore();
            btnColors[clickedIndex].animate().scaleX(0.9f).scaleY(0.9f).setDuration(120)
                    .withEndAction(() -> btnColors[clickedIndex].animate().scaleX(1f).scaleY(1f).setDuration(120).start()).start();
            handler.postDelayed(this::nextRound, 300);
        } else {
            // wrong
            flashRedOverlay();
            safeSetViewColor(btnColors[clickedIndex], Color.BLACK, false);
            handler.postDelayed(() -> {
                safeSetViewColor(btnColors[clickedIndex], COLORS[clickedIndex], false);
                nextRound();
            }, 500);
        }
    }

    private void flashRedOverlay() {
        redOverlay.setAlpha(0f);
        redOverlay.setVisibility(View.VISIBLE);
        redOverlay.animate().alpha(1f).setDuration(100).withEndAction(() ->
                redOverlay.animate().alpha(0f).setDuration(250).withEndAction(() ->
                        redOverlay.setVisibility(View.GONE)).start()).start();
    }

    // SAFER background color setter:
    // - if existing background is GradientDrawable, update its color (keeps shape)
    // - otherwise create and set a new appropriate GradientDrawable (oval or rounded rect)
    private void safeSetViewColor(View v, int color, boolean makeOval) {
        if (v == null) return;
        Drawable bg = v.getBackground();
        if (bg instanceof GradientDrawable) {
            try {
                ((GradientDrawable) bg).setColor(color);
                v.setBackground(bg); // re-apply
                return;
            } catch (Exception ignored) { }
        }

        // fallback: create new drawable
        GradientDrawable gd = new GradientDrawable();
        if (makeOval) {
            gd.setShape(GradientDrawable.OVAL);
        } else {
            gd.setShape(GradientDrawable.RECTANGLE);
            gd.setCornerRadius(dpToPx(8));
        }
        gd.setColor(color);
        v.setBackground(gd);
    }

    private void updateScore() {
        tvScore.setText(String.valueOf(score));
    }

    private void endGame() {
        roundActive = false;
        if (roundTimer != null) roundTimer.cancel();
        if (scaleAnimX != null) scaleAnimX.cancel();
        if (scaleAnimY != null) scaleAnimY.cancel();

        boolean passed = score >= PASS_THRESHOLD;
        String t = passed ? "Level Passed" : "Level Failed";
        String m = passed ? "Great! Your score: " + score : "Try again. Your score: " + score;

        new AlertDialog.Builder(this)
                .setTitle(t)
                .setMessage(m)
                .setCancelable(false)
                .setPositiveButton(passed ? "Next" : "Retry", (dialog, which) -> {
                    if (passed) finish();
                    else startGame();
                })
                .setNegativeButton("Home", (dialog, which) -> finish())
                .show();
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (roundTimer != null) roundTimer.cancel();
        handler.removeCallbacksAndMessages(null);
    }
}
