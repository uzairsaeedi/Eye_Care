package com.eyetest.eyecare.eyesighttest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SnellenChartActivity extends BaseActivity {

    ImageView ivBack, ivIllustration;
    TextView tvTitle, tvInstructions;
    Button btnStartTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snellen_chart);

        ivBack = findViewById(R.id.ivBack);
        ivIllustration = findViewById(R.id.ivIllustration);
        btnStartTest = findViewById(R.id.btnStartTest);
        tvTitle = findViewById(R.id.tvTitle);
        tvInstructions = findViewById(R.id.tvInstruction);

        View root = findViewById(R.id.scrollRoot);

        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            int topInset = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top;

            v.setPadding(v.getPaddingLeft(), topInset, v.getPaddingRight(), v.getPaddingBottom());

            return WindowInsetsCompat.CONSUMED;
        });

        Intent i = getIntent();
        String exerciseType = i.getStringExtra("exerciseType");
        String title = i.getStringExtra("title");
        int iconRes = i.getIntExtra("iconRes", R.mipmap.ic_launcher);

        if (title != null) tvTitle.setText(title);
        ivIllustration.setImageResource(iconRes);

        String instr = InstructioinsBank.getInstructions(exerciseType);
        tvInstructions.setText(instr);

        ivBack.setOnClickListener(v -> finish());

        btnStartTest.setOnClickListener(v -> {
            Intent quiz;
            if ("visual_acuity".equals(exerciseType)) {
                quiz = new Intent(SnellenChartActivity.this, VisualAcuity.class);
            } else if ("astigmatism_test".equals(exerciseType)) {
                quiz = new Intent(SnellenChartActivity.this, AstigmatismTestActivity.class);
            } else if ("okn_test".equals(exerciseType)) {
                quiz = new Intent(SnellenChartActivity.this, OKNTestActivity.class);
            } else if ("landolt".equals(exerciseType)) {
                quiz = new Intent(SnellenChartActivity.this, LandoltActivity.class);
            } else if ("eye_muscle_training".equals(exerciseType)) {
                quiz = new Intent(SnellenChartActivity.this, EyeMuscleTraining.class);
            } else if ("focus_shift".equals(exerciseType)) {
                quiz = new Intent(SnellenChartActivity.this, FocusShift.class);
            } else if ("hyperopia_prevention".equals(exerciseType)) {
                quiz = new Intent(SnellenChartActivity.this, HyperopiaPrevention.class);
            } else if ("kaleidoscope".equals(exerciseType)) {
                quiz = new Intent(SnellenChartActivity.this, KaleidoscopeActivity.class);
            } else if ("closed_eye_movements".equals(exerciseType)) {
                quiz = new Intent(SnellenChartActivity.this, ClosedEyeMovement.class);
            } else if ("palming".equals(exerciseType)) {
                quiz = new Intent(SnellenChartActivity.this, PalminActivity.class);
            } else if ("track_animal".equals(exerciseType)) {
                quiz = new Intent(SnellenChartActivity.this, TrackAnimalActivity.class);
            } else if ("color_cube".equals(exerciseType)) {
                quiz = new Intent(SnellenChartActivity.this, ColorCubeActivity.class);
            } else if ("color_match".equals(exerciseType)) {
                quiz = new Intent(SnellenChartActivity.this, ColorMatchActivity.class);
            } else if ("odd_one_out".equals(exerciseType)) {
                quiz = new Intent(SnellenChartActivity.this, OddOneOutActivity.class);
            } else if ("lazy_eye".equals(exerciseType)) {
                quiz = new Intent(SnellenChartActivity.this, LazyEyeActivity.class);
            } else {
                quiz = new Intent(SnellenChartActivity.this, EyeTestActivity.class);
            }
            quiz.putExtra("exerciseType", exerciseType);
            startActivity(quiz);
        });
    }
}