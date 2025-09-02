package com.eyecare;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class OddOneOutActivity extends BaseActivity {

    private GridLayout grid;
    private TextView tvScore, tvTimer;
    private View ivBack;
    private View redOverlay;

    private final int COLS = 4;
    private final int ROWS = 4;
    private final int TOTAL = COLS * ROWS;

    private final int ROUNDS = 6;
    private final int ROUND_SECONDS = 10;
    private final int PASS_THRESHOLD = 4;

    private final Handler handler = new Handler();
    private final Random rnd = new Random();

    private AnimalView[] items = new AnimalView[TOTAL];
    private int oddIndex = -1;
    private int currentRound = 0;
    private int score = 0;

    private CountDownTimer roundTimer;
    private int currentAnimalType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odd_one_out);

        grid = findViewById(R.id.grid);
        tvScore = findViewById(R.id.tvScore);
        tvTimer = findViewById(R.id.tvTimer);
        ivBack = findViewById(R.id.ivBack);
        redOverlay = findViewById(R.id.redOverlay);

        ivBack.setOnClickListener(v -> finish());

        grid.post(this::buildGrid);
    }

    private void buildGrid() {
        grid.removeAllViews();
        grid.setColumnCount(COLS);
        grid.setRowCount(ROWS);

        int padding = dpToPx(6);

        for (int i = 0; i < TOTAL; i++) {
            AnimalView v = new AnimalView(this);

            GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
            lp.width = 0; // let weight handle width
            lp.height = 0; // let weight handle height
            lp.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            lp.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            lp.setMargins(padding, padding, padding, padding);

            v.setLayoutParams(lp);
            v.setAnimalType(0);
            v.setVariant(0);

            final int idx = i;
            v.setOnClickListener(view -> onItemClick(idx));

            grid.addView(v);
            items[i] = v;
        }

        handler.postDelayed(this::startNextRound, 150);
    }


    private void startNextRound() {
        if (roundTimer != null) { roundTimer.cancel(); roundTimer = null; }

        if (currentRound >= ROUNDS) {
            showResult();
            return;
        }

        currentRound++;
        // pick a random animal type each round (0..4)
        currentAnimalType = rnd.nextInt(5);

        // choose random odd index and odd variant
        oddIndex = rnd.nextInt(TOTAL);
        int oddVariant = 1 + rnd.nextInt(3);

        // set all items to same animal type normal
        for (int i = 0; i < TOTAL; i++) {
            items[i].setAnimalType(currentAnimalType);
            items[i].setVariant(0);
            items[i].setEnabled(true);
            items[i].setAlpha(1f);
        }
        // set odd one
        items[oddIndex].setVariant(oddVariant);

        // start timer
        final int seconds = ROUND_SECONDS;
        tvTimer.setText(String.valueOf(seconds));
        roundTimer = new CountDownTimer(seconds * 1000L, 1000) {
            int secLeft = seconds;
            @Override public void onTick(long millisUntilFinished) {
                secLeft--;
                tvTimer.setText(String.valueOf(Math.max(0, secLeft)));
            }
            @Override public void onFinish() {
                tvTimer.setText("0");
                handler.postDelayed(() -> startNextRound(), 400);
            }
        }.start();
    }

    private void onItemClick(int idx) {
        // disable further clicks until handled
        for (AnimalView av : items) if (av != null) av.setEnabled(false);

        if (roundTimer != null) { roundTimer.cancel(); roundTimer = null; }

        if (idx == oddIndex) {
            // correct
            score++;
            tvScore.setText(String.valueOf(score));
            items[idx].animate().scaleX(0.9f).scaleY(0.9f).setDuration(140)
                    .withEndAction(() -> items[idx].animate().scaleX(1f).scaleY(1f).setDuration(140).start()).start();
            handler.postDelayed(this::startNextRound, 300);
        } else {
            // wrong
            flashRed();
            items[idx].setAlpha(0.3f);
            handler.postDelayed(() -> startNextRound(), 500);
        }
    }

    private void flashRed() {
        redOverlay.setAlpha(0f);
        redOverlay.setVisibility(View.VISIBLE);
        redOverlay.animate().alpha(1f).setDuration(90).withEndAction(() ->
                redOverlay.animate().alpha(0f).setDuration(220).withEndAction(() ->
                        redOverlay.setVisibility(View.GONE)).start()).start();
    }

    private void showResult() {
        if (roundTimer != null) { roundTimer.cancel(); roundTimer = null; }
        boolean passed = score >= PASS_THRESHOLD;
        String title = passed ? "Level Passed" : "Level Failed";
        String msg = passed ? "Great! Score: " + score + "/" + ROUNDS : "Try again. Score: " + score + "/" + ROUNDS;

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(passed ? "Finish" : "Retry", (d, w) -> {
                    if (passed) finish();
                    else {
                        score = 0;
                        tvScore.setText("0");
                        currentRound = 0;
                        handler.postDelayed(this::startNextRound, 200);
                    }
                })
                .setNegativeButton("Home", (d, w) -> finish())
                .show();
    }

    private int dpToPx(int dp) {
        return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (roundTimer != null) roundTimer.cancel();
        handler.removeCallbacksAndMessages(null);
    }
}
