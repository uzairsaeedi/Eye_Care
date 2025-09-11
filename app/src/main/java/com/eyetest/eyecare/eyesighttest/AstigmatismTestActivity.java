package com.eyetest.eyecare.eyesighttest;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AstigmatismTestActivity extends BaseActivity {

    private ImageView imageView, ivBack;
    private Button btnNext;
    private TextView tvNote;
    private int currentIndex = 0;

    private int[] questions = {
            R.mipmap.astigmatism_1_foreground,
            R.mipmap.astigmatism_2_foreground,
            R.mipmap.astigmatism_3_foreground,
            R.mipmap.astigmatism_4_foreground
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_astigmatism_test);

        ivBack = findViewById(R.id.ivBack);
        imageView = findViewById(R.id.imageViewAstigmatism);
        btnNext = findViewById(R.id.btnNext);
        tvNote = findViewById(R.id.tvNote);

        showQuestion();

        ivBack.setOnClickListener(v -> finish());

        btnNext.setOnClickListener(v -> {
            if (currentIndex < questions.length - 1) {
                currentIndex++;
                showQuestion();

                if (currentIndex == questions.length - 1) {
                    btnNext.setText("Finish");
                    tvNote.setVisibility(View.VISIBLE);
                }
            } else {
                finish();
            }
        });
    }

    private void showQuestion() {
        imageView.setImageResource(questions[currentIndex]);
    }
}
