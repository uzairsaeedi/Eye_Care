package com.eyecare;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class EyeTestActivity extends BaseActivity {

    private LinearLayout rootLayout;
    private ImageView ivBack, ivPlate;
    private TextView tvQuestionCount, tvInstruction, tvLetters;
    private Button btnOption1, btnOption2, btnOption3, btnNext;

    private List<Question> questions;
    private int current = 0;
    private int score = 0;

    private final Handler handler = new Handler();

    private float baseFontSize = 32f;
    private float minFontSize = 10f;
    private float decrement = 5f;

    private String[] contrastLevels = {
            "#333333", // highest contrast (dark)
            "#555555",
            "#777777",
            "#999999",
            "#BBBBBB",
            "#DDDDDD"  // lowest contrast (very light)
    };

    private float contrastFontSize = 48f;

    // which exercise we're running (set from intent)
    private String exerciseType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eye_test);

        rootLayout = findViewById(R.id.rootLayout);
        ivBack = findViewById(R.id.ivBack);
        ivPlate = findViewById(R.id.ivPlate);
        tvQuestionCount = findViewById(R.id.tvQuestionCount);
        tvInstruction = findViewById(R.id.tvInstruction);
        tvLetters = findViewById(R.id.tvLetters);
        btnOption1 = findViewById(R.id.btnOption1);
        btnOption2 = findViewById(R.id.btnOption2);
        btnOption3 = findViewById(R.id.btnOption3);
        btnNext = findViewById(R.id.btnNext);

        ivBack.setOnClickListener(v -> finish());

        exerciseType = getIntent().getStringExtra("exerciseType");
        if (exerciseType == null) exerciseType = "snellen_chart";

        questions = QuestionBank.getQuestions(exerciseType);

        setupQuestion();

        View.OnClickListener optionClick = v -> {
            Button b = (Button) v;
            btnOption1.setEnabled(false);
            btnOption2.setEnabled(false);
            btnOption3.setEnabled(false);
            checkAnswer(b.getText().toString());
        };

        btnOption1.setOnClickListener(optionClick);
        btnOption2.setOnClickListener(optionClick);
        btnOption3.setOnClickListener(optionClick);

        btnNext.setOnClickListener(v -> {
            current++;
            if (current < questions.size()) {
                setupQuestion();
            } else {
                goToResult();
            }
        });
    }

    private void setupQuestion() {
        btnOption1.setEnabled(true);
        btnOption2.setEnabled(true);
        btnOption3.setEnabled(true);

        if (current >= questions.size()) {
            goToResult();
            return;
        }

        Question q = questions.get(current);

        tvQuestionCount.setText("Q. " + (current + 1) + "/" + questions.size());

        if (q.getImageResId() != 0) {
            ivPlate.setVisibility(View.VISIBLE);
            tvLetters.setVisibility(View.GONE);
            ivPlate.setImageResource(q.getImageResId());

            resetLetterAppearance();
            btnOption1.setText(q.getOption1());
            btnOption2.setText(q.getOption2());
            btnOption3.setText(q.getOption3());
        } else {
            ivPlate.setVisibility(View.GONE);
            tvLetters.setVisibility(View.VISIBLE);

            tvLetters.setText(q.getMainLetters());

            btnOption1.setText(q.getOption1());
            btnOption2.setText(q.getOption2());
            btnOption3.setText(q.getOption3());

            if ("contrast_sensitivity".equalsIgnoreCase(exerciseType)) {
                tvLetters.setTextSize(TypedValue.COMPLEX_UNIT_SP, contrastFontSize);

                int colorIndex = Math.min(current, contrastLevels.length - 1);
                try {
                    tvLetters.setTextColor(Color.parseColor(contrastLevels[colorIndex]));
                } catch (Exception e) {
                    tvLetters.setTextColor(Color.parseColor("#777777")); // fallback
                }

                float alpha = 1.0f;
                if (contrastLevels.length > 1) {
                    alpha = 1.0f - (0.5f * ((float) colorIndex / (contrastLevels.length - 1)));
                }
                tvLetters.setAlpha(alpha);
            } else {
                float newFontSize = baseFontSize - (current * decrement);
                if (newFontSize < minFontSize) newFontSize = minFontSize;
                tvLetters.setTextSize(TypedValue.COMPLEX_UNIT_SP, newFontSize);

                tvLetters.setTextColor(Color.parseColor("#333333"));
                tvLetters.setAlpha(1f);
            }
        }

        rootLayout.setBackgroundColor(Color.WHITE);
    }

    private void checkAnswer(String selected) {
        Question q = questions.get(current);

        boolean correct = selected.equals(q.getCorrect());
        if (correct) {
            score++;
            flashColor("#C8E6C9");
        } else {
            flashColor("#FFCDD2");
        }

        handler.postDelayed(() -> {
            rootLayout.setBackgroundColor(Color.WHITE);
            current++;
            if (current < questions.size()) {
                setupQuestion();
            } else {
                goToResult();
            }
        }, 450);
    }

    private void flashColor(String colorHex) {
        try {
            rootLayout.setBackgroundColor(Color.parseColor(colorHex));
        } catch (Exception e) {
            rootLayout.setBackgroundColor(Color.WHITE);
        }
    }

    private void resetLetterAppearance() {
        tvLetters.setTextColor(Color.parseColor("#333333"));
        tvLetters.setAlpha(1f);
        tvLetters.setTextSize(TypedValue.COMPLEX_UNIT_SP, baseFontSize);
    }

    private void goToResult() {
        String exercise = exerciseType;
        if (exercise == null) exercise = "default";

        ProgressUtils.markTestCompleted(this, exercise);

        Intent i = new Intent(EyeTestActivity.this, ResultActivity.class);
        i.putExtra("score", score);
        i.putExtra("total", questions.size());
        i.putExtra("exerciseType", exercise);
        startActivity(i);
        finish();
    }
}
