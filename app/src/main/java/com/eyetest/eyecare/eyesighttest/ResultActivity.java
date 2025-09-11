package com.eyetest.eyecare.eyesighttest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView tvResult = findViewById(R.id.tvResult);
        Button btnRetry = findViewById(R.id.btnRetry);
        Button btnFinish = findViewById(R.id.btnFinish);

        int score = getIntent().getIntExtra("score", 0);
        int total = getIntent().getIntExtra("total", 0);

        tvResult.setText("Your Score: " + score + " / " + total);

        String exerciseType = getIntent().getStringExtra("exerciseType");

        btnRetry.setOnClickListener(v -> {
            Intent i;

            if ("visual_acuity".equals(exerciseType)) {
                i = new Intent(ResultActivity.this, VisualAcuity.class);
            } else {
                i = new Intent(ResultActivity.this, EyeTestActivity.class);
            }

            i.putExtra("exerciseType", exerciseType);

            startActivity(i);
            finish();
        });

        btnFinish.setOnClickListener(v -> finish());
    }
}