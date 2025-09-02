package com.eyecare;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class VisualAcuity extends BaseActivity {

    private TextView tvNumber;
    private Button btnCheck;

    private final List<String[]> questions = new ArrayList<>();

    private int current = 0;
    private int score = 0;

    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visual_acuity);

        tvNumber = findViewById(R.id.tvNumber);
        btnCheck = findViewById(R.id.btnCheck);

        questions.add(new String[]{"57", "57"});
        questions.add(new String[]{"60 36 0", "60 36 0"}); // user may enter "60 36 0" or "60,36,0" (we will normalize)
        questions.add(new String[]{"100", "100"});
        questions.add(new String[]{"85", "85"});
        questions.add(new String[]{"20", "20"});

        showQuestion();

        btnCheck.setOnClickListener(v -> {
            askUserAnswer();
        });
    }

    private void showQuestion() {
        if (current >= questions.size()) {
            goToResult();
            return;
        }
        String[] q = questions.get(current);
        String display = q[0];
        tvNumber.setText(display);
    }

    private void askUserAnswer() {
        // EditText for user input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setGravity(Gravity.CENTER);

        AlertDialog.Builder b = new AlertDialog.Builder(this)
                .setTitle("Enter the number you saw")
                .setView(input)
                .setCancelable(true)
                .setPositiveButton("Submit", (dialog, which) -> {
                    String userText = input.getText() == null ? "" : input.getText().toString().trim();
                    checkAnswer(userText);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                });

        AlertDialog dlg = b.create();
        dlg.show();
    }

    private void checkAnswer(String userInput) {
        String[] q = questions.get(current);
        String correct = q[1];

        String normUser = normalizeNumberString(userInput);
        String normCorrect = normalizeNumberString(correct);

        boolean correctAnswer = normUser.equalsIgnoreCase(normCorrect);

        if (correctAnswer) {
            score++;
            Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Wrong (Correct: " + correct + ")", Toast.LENGTH_SHORT).show();
        }

        current++;
        if (current < questions.size()) {
            showQuestion();
        } else {
            goToResult();
        }
    }

    private String normalizeNumberString(String s) {
        if (s == null) return "";
        s = s.replaceAll("[,;/\\\\]", " ");
        s = s.replaceAll("\\s+", " ").trim();
        return s;
    }

    private void goToResult() {
        String exerciseType = getIntent().getStringExtra("exerciseType");
        if (exerciseType == null) exerciseType = "visual_acuity";

        ProgressUtils.markTestCompleted(this, exerciseType);

        Intent i = new Intent(VisualAcuity.this, ResultActivity.class);
        i.putExtra("score", score);
        i.putExtra("total", questions.size());
        i.putExtra("exerciseType", exerciseType); // optional, for ResultActivity to show which exercise
        startActivity(i);
        finish();
    }
}