package com.eyecare;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.Locale;

public class ClosedEyeMovement extends BaseActivity {

    private static final String TAG = "ClosedEyeMovement";

    private TextView timerText;
    private int totalTime = 30;
    private CircularProgressIndicator circleProgress;
    private ImageView ivPupil, ivBack;
    private TextView tvInstruction;
    private Handler handler = new Handler();
    private TextToSpeech tts;
    private final long STEP_DELAY_MS = 5000L; // 5 seconds between steps
    private final long START_DELAY_MS = 2000L;

    private final String[] instructions = {
            "Move your eyes to the right",
            "Move your eyes to the left",
            "Move your eyes up",
            "Move your eyes down"
    };

    private int index = 0;
    private float moveX;
    private float moveY;

    private boolean isRunning = false; // sequence running flag
    private View eyeContainer;

    private final Runnable stepRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isRunning) {
                Log.d(TAG, "Runnable skipped because not running");
                return;
            }

            showStep(index);
            index = (index + 1) % instructions.length;
            handler.postDelayed(this, STEP_DELAY_MS);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closed_eye_movement);

        ivBack = findViewById(R.id.ivBack);
        ivPupil = findViewById(R.id.iv_pupil);
        tvInstruction = findViewById(R.id.tvInstruction);
        timerText = findViewById(R.id.timerText);
        circleProgress = findViewById(R.id.circleProgress);
        eyeContainer = findViewById(R.id.eye_container);

        ivBack.setOnClickListener(v -> {
            stopSequenceAndTts();
            finish();
        });

        circleProgress.setMax(totalTime);
        circleProgress.setProgress(totalTime);

        startTimer();

        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.getDefault());
                tts.setSpeechRate(1.0f);
                startSequenceWhenReady();
            } else {
                Log.w(TAG, "TTS init failed status=" + status);
                startSequenceWhenReady();
            }
        });
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
                stopSequenceAndTts();
                Intent intent = new Intent(ClosedEyeMovement.this, Congratulations.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }

    private void startSequenceWhenReady() {
        if (eyeContainer == null) {
            Log.w(TAG, "eyeContainer is null, cannot compute moves");
            return;
        }

        if (eyeContainer.getWidth() > 0 && eyeContainer.getHeight() > 0 && ivPupil.getWidth() > 0) {
            computeMoveDistancesAndStart();
            return;
        }

        eyeContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                eyeContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                computeMoveDistancesAndStart();
            }
        });
    }

    private void computeMoveDistancesAndStart() {
        float containerW = eyeContainer.getWidth();
        float containerH = eyeContainer.getHeight();
        float pupilW = ivPupil.getWidth();
        float pupilH = ivPupil.getHeight();

        if (containerW <= 0 || containerH <= 0 || pupilW <= 0 || pupilH <= 0) {
            moveX = 50f;
            moveY = 30f;
        } else {
            moveX = (containerW - pupilW) * 0.35f;
            moveY = (containerH - pupilH) * 0.35f;
        }

        Log.d(TAG, "Computed moves: moveX=" + moveX + " moveY=" + moveY);

        if (!isRunning) {
            isRunning = true;
            index = 0;

            handler.postDelayed(stepRunnable, START_DELAY_MS);
            Log.d(TAG, "Sequence will start in " + START_DELAY_MS + " ms");
        } else {
            Log.d(TAG, "Sequence already running; not starting again");
        }
    }

    private void showStep(int idx) {
        final String text = instructions[idx];
        tvInstruction.setText(text);

        Log.d(TAG, "Step: " + text + " (idx=" + idx + ")");

        if (tts != null) {
            try {
                tts.stop();
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "INSTR_" + idx);
            } catch (Exception e) {
                Log.w(TAG, "TTS speak error: " + e.getMessage());
            }
        }

        switch (idx) {
            case 0:
                ivPupil.animate().translationX(moveX).translationY(0).setDuration(600).start();
                break;
            case 1:
                ivPupil.animate().translationX(-moveX).translationY(0).setDuration(600).start();
                break;
            case 2:
                ivPupil.animate().translationX(0).translationY(-moveY).setDuration(600).start();
                break;
            case 3:
                ivPupil.animate().translationX(0).translationY(moveY).setDuration(600).start();
                break;
            default:
                ivPupil.animate().translationX(0).translationY(0).setDuration(600).start();
                break;
        }
    }

    private void stopSequenceAndTts() {
        isRunning = false;
        handler.removeCallbacks(stepRunnable);

        if (tts != null) {
            try {
                tts.stop();
            } catch (Exception ignored) { }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopSequenceAndTts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isRunning) {
            startSequenceWhenReady();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopSequenceAndTts();
        if (tts != null) {
            try {
                tts.shutdown();
            } catch (Exception ignored) { }
            tts = null;
        }
    }
}
