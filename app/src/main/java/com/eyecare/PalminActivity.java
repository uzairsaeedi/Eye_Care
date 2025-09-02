package com.eyecare;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.Locale;

public class PalminActivity extends BaseActivity {

    private static final String TAG = "PalminActivity";

    private static final long STEP_SECONDS = 10;
    private static final long TTS_DELAY_MS = 1000L;
    private CountDownTimer countDownTimer;
    private long secondsLeft = STEP_SECONDS;

    private CircularProgressIndicator circleProgress;
    private TextView timerText;
    private ImageView ivExerciseImage;
    private TextView tvInstruction;
    private ImageView ivBack;

    private final int[] images = { R.mipmap.palming1_foreground, R.mipmap.palming2_foreground };
    private final String[] instructions = { "Rub Your Hands", "Cover your eyes with your palms." };

    private int stateIndex = 0;
    private int shownCount = 0;

    private TextToSpeech tts;
    private final Handler handler = new Handler();
    private final Runnable speakRunnable = new Runnable() {
        @Override
        public void run() {
            speakCurrentInstruction();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palmin);

        circleProgress = findViewById(R.id.circleProgress);
        timerText = findViewById(R.id.timerText);
        ivExerciseImage = findViewById(R.id.ivExerciseImage);
        tvInstruction = findViewById(R.id.tvInstruction);
        ivBack = findViewById(R.id.ivBack);

        updateUiForState();

        ivBack.setOnClickListener(v -> {
            stopTimer();
            releaseTts();
            finish();
        });

        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                try {
                    tts.setLanguage(Locale.getDefault());
                } catch (Exception e) {
                    Log.w(TAG, "TTS setLanguage failed: " + e.getMessage());
                }
                tts.setSpeechRate(1.0f);
                // schedule first speak
                scheduleSpeakWithDelay();
            } else {
                Log.w(TAG, "TTS init failed: status=" + status);
            }
        });

        startStepTimer();
    }

    private void updateUiForState() {
        ivExerciseImage.setImageResource(images[stateIndex]);
        tvInstruction.setText(instructions[stateIndex]);

        circleProgress.setMax((int) STEP_SECONDS);
        circleProgress.setProgress((int) STEP_SECONDS);
        timerText.setText(String.valueOf((int) STEP_SECONDS));

        scheduleSpeakWithDelay();
    }

    private void scheduleSpeakWithDelay() {
        handler.removeCallbacks(speakRunnable);

        if (tts != null) {
            try { tts.stop(); } catch (Exception ignored) {}
        }

        handler.postDelayed(speakRunnable, TTS_DELAY_MS);
    }

    private void speakCurrentInstruction() {
        if (tts == null) return;
        String text = instructions[stateIndex];
        try {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "INSTR_" + stateIndex);
        } catch (Exception e) {
            Log.w(TAG, "TTS speak error: " + e.getMessage());
        }
    }

    private void startStepTimer() {
        stopTimer();
        secondsLeft = STEP_SECONDS;
        countDownTimer = new CountDownTimer(STEP_SECONDS * 1000L, 1000L) {
            @Override
            public void onTick(long millisUntilFinished) {
                secondsLeft = millisUntilFinished / 1000L;
                int sec = (int) secondsLeft;
                timerText.setText(String.valueOf(sec));
                circleProgress.setProgressCompat(sec, true); // animate
            }

            @Override
            public void onFinish() {
                shownCount++;

                if (shownCount >= images.length) {
                    stopTimer();
                    releaseTts();
                    Intent intent = new Intent(PalminActivity.this, Congratulations.class);
                    startActivity(intent);
                    finish();
                    return;
                }

                stateIndex = (stateIndex + 1) % images.length;
                updateUiForState();
                startStepTimer();
            }
        }.start();
    }

    private void stopTimer() {
        if (countDownTimer != null) {
            try {
                countDownTimer.cancel();
            } catch (Exception ignored) {}
            countDownTimer = null;
        }
    }

    private void stopTtsAndCallbacks() {
        handler.removeCallbacks(speakRunnable);

        if (tts != null) {
            try {
                tts.stop();
            } catch (Exception ignored) {}
        }
    }

    /**
     * Fully release TTS (stop + shutdown) and clear handler callbacks.
     * Call this when activity is finishing or you want to completely free TTS resources.
     */
    private void releaseTts() {
        handler.removeCallbacksAndMessages(null);
        if (tts != null) {
            try {
                tts.stop();
            } catch (Exception ignored) {}
            try {
                tts.shutdown();
            } catch (Exception ignored) {}
            tts = null;
        }
    }

    @Override
    public void onBackPressed() {
        // user pressed system back -> stop timer & tts and then finish
        stopTimer();
        releaseTts();
        super.onBackPressed(); // will finish()
    }

    @Override
    protected void onPause() {
        super.onPause();
        // temporary stop when activity not visible
        stopTimer();
        stopTtsAndCallbacks();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (shownCount < images.length) {
            startStepTimer();
            scheduleSpeakWithDelay();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
        releaseTts();
    }
}
