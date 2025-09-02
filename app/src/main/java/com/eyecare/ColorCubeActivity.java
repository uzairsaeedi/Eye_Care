package com.eyecare;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class ColorCubeActivity extends BaseActivity {

    private GridLayout grid;
    private TextView tvScore, tvTimer;
    private View ivBack;
    private final Handler handler = new Handler();

    // config
    private final int COLS = 4;
    private final int ROWS = 4;
    private final int TOTAL_CELLS = COLS * ROWS;
    private final long ROUND_DURATION_MS = 5_000;
    private final long TOTAL_DURATION_MS = 30_000;
    private final int PASS_THRESHOLD = 6;

    // state
    private View[] cells;
    private int oddIndex = 0;
    private boolean roundActive = false;
    private boolean clickLocked = false;
    private int score = 0;
    private CountDownTimer totalTimer;
    private CountDownTimer roundTimer;
    private final Random rnd = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_cube);

        grid = findViewById(R.id.grid);
        tvScore = findViewById(R.id.tvScore);
        tvTimer = findViewById(R.id.tvTimer);
        ivBack = findViewById(R.id.ivBack);

        ivBack.setOnClickListener(v -> finish());

        setupGrid();
        startGame();

    }

    private void setupGrid() {
        grid.removeAllViews();
        grid.setColumnCount(COLS);
        cells = new View[TOTAL_CELLS];

        int parentPadding = dpToPx(12) * 2;
        int screenWidth = getResources().getDisplayMetrics().widthPixels;

        int gridWidth = screenWidth - parentPadding;

        int cellSize = gridWidth / COLS;

        for (int i = 0; i < TOTAL_CELLS; i++) {
            View v = new View(this);
            GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
            lp.width = cellSize;
            lp.height = cellSize;

            int spacing = dpToPx(4);
            lp.setMargins(spacing, spacing, spacing, spacing);

            v.setLayoutParams(lp);

            GradientDrawable bg = new GradientDrawable();
            bg.setCornerRadius(dpToPx(8));
            bg.setColor(Color.LTGRAY);
            v.setBackground(bg);

            final int index = i;
            v.setOnClickListener(view -> {
                if (!roundActive || clickLocked) return;
                clickLocked = true;
                handleCellClick(index, view);
            });

            grid.addView(v);
            cells[i] = v;
        }
    }

    private void startGame() {
        score = 0;
        updateScore();
        startTotalTimer();
        startRound();
    }

    private void startTotalTimer() {
        if (totalTimer != null) totalTimer.cancel();
        totalTimer = new CountDownTimer(TOTAL_DURATION_MS, 1000) {
            @Override public void onTick(long millisUntilFinished) {
                int s = (int) (millisUntilFinished / 1000);
                tvTimer.setText(String.valueOf(s));
            }
            @Override public void onFinish() {
                tvTimer.setText("0");
                endGame();
            }
        }.start();
    }

    private void startRound() {
        roundActive = true;
        clickLocked = false;
        // pick base color and odd color
        float[] hsv = new float[3];
        hsv[0] = rnd.nextInt(360);               // hue
        hsv[1] = 0.5f + rnd.nextFloat() * 0.4f;  // sat 0.5..0.9
        hsv[2] = 0.4f + rnd.nextFloat() * 0.45f; // val 0.4..0.85

        int baseColor = Color.HSVToColor(hsv);

        // create odd color by slightly changing value or saturation slightly
        float[] oddHsv = hsv.clone();
        float delta = (rnd.nextBoolean() ? -1 : 1) * (0.06f + rnd.nextFloat() * 0.08f); // 0.06..0.14
        oddHsv[2] = clamp01(oddHsv[2] + delta);
        int oddColor = Color.HSVToColor(oddHsv);

        // choose odd index
        oddIndex = rnd.nextInt(TOTAL_CELLS);

        // apply colors
        for (int i = 0; i < TOTAL_CELLS; i++) {
            setCellColor(cells[i], (i == oddIndex) ? oddColor : baseColor);
            // reset alpha/enable
            cells[i].setAlpha(1f);
            cells[i].setEnabled(true);
        }

        if (roundTimer != null) roundTimer.cancel();
        roundTimer = new CountDownTimer(ROUND_DURATION_MS, ROUND_DURATION_MS) {
            @Override public void onTick(long millisUntilFinished) { }
            @Override public void onFinish() {
                // round ended without click -> just move to next round
                clickLocked = true;
                // small delay then new round
                handler.postDelayed(() -> {
                    if (totalTimer != null) {
                        // continue only if overall time not finished
                        startRound();
                    }
                }, 200);
            }
        }.start();
    }
    private void handleCellClick(int index, View view) {
        // cancel round timer when user clicks
        if (roundTimer != null) {
            roundTimer.cancel();
            roundTimer = null;
        }

        if (index == oddIndex) {
            // correct
            score++;
            updateScore();

            // show a small visual feedback (decrease alpha then next)
            view.animate().scaleX(0.92f).scaleY(0.92f).setDuration(120)
                    .withEndAction(() -> view.animate().scaleX(1f).scaleY(1f).setDuration(120).start()).start();

            // immediately start next round after short delay
            handler.postDelayed(this::startRound, 220);

        } else {
            // wrong -> make this clicked cell black and continue to next round
            setCellColor(view, Color.BLACK);
            view.setEnabled(false);
            // small delay so user sees black effect
            handler.postDelayed(this::startRound, 500);
        }
    }

    private void setCellColor(View v, int color) {
        // background is GradientDrawable; replace color programmatically
        GradientDrawable gd = new GradientDrawable();
        gd.setCornerRadius(dpToPx(8));
        gd.setColor(color);
        v.setBackground(gd);
    }

    private void updateScore() {
        tvScore.setText(String.valueOf(score));
    }

    private void endGame() {
        roundActive = false;
        clickLocked = true;
        if (roundTimer != null) roundTimer.cancel();
        if (totalTimer != null) totalTimer.cancel();

        boolean passed = score >= PASS_THRESHOLD;
        String title = passed ? "Level Passed" : "Level Failed";
        String message = passed ? "Great! You scored " + score + "." : "Try again. You scored " + score + ".";

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(passed ? "Next" : "Retry", (DialogInterface dialog, int which) -> {
                    if (passed) {
                        finish(); // or go next level
                    } else {
                        restartGame();
                    }
                })
                .setNegativeButton("Home", (DialogInterface dialog, int which) -> finish())
                .show();
    }

    private void restartGame() {
        // reset
        score = 0;
        updateScore();
        startGame();
    }

    private float clamp01(float v) {
        if (v < 0f) return 0f;
        if (v > 1f) return 1f;
        return v;
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (totalTimer != null) totalTimer.cancel();
        if (roundTimer != null) roundTimer.cancel();
        handler.removeCallbacksAndMessages(null);
    }
}