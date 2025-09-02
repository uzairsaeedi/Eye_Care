package com.eyecare;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EyeMuscleTraining extends BaseActivity {

    private View dot;
    private TextView instruction;
    private Handler handler = new Handler();
    private int step = 0;

    private String[] directions = {
            "Move your Eye to Right Direction",
            "Move your Eye to Left Direction",
            "Move your Eye to Top Right Direction",
            "Move your Eye to Top Left Direction",
            "Move your Eye to Bottom Right Direction",
            "Move your Eye to Bottom Left Direction",
            "Move your Eye to Down Direction",
            "Move your Eye to Up Direction"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eye_muscle_training);

        dot = findViewById(R.id.dot);
        instruction = findViewById(R.id.instruction);

        startExercise();
    }

    private void startExercise() {
        runStep();
    }

    private void runStep() {
        if (step < directions.length) {
            instruction.setText(directions[step]);

            // Animate dot
            float moveX = 0, moveY = 0;
            int distance = 600; // adjust for screen size

            switch (step) {
                case 0: // Right
                    moveX = distance;
                    break;
                case 1: // Left
                    moveX = -distance;
                    break;
                case 2: // Top Right
                    moveX = distance;
                    moveY = -distance;
                    break;
                case 3: // Top Left
                    moveX = -distance;
                    moveY = -distance;
                    break;
                case 4: // Bottom Right
                    moveX = distance;
                    moveY = distance;
                    break;
                case 5: // Bottom Left
                    moveX = -distance;
                    moveY = distance;
                    break;
                case 6: // Down
                    moveY = distance;
                    break;
                case 7: // Up
                    moveY = -distance;
                    break;
            }

            dot.animate().translationX(moveX).translationY(moveY).setDuration(2000).withEndAction(() -> {
                step++;
                handler.postDelayed(this::runStep, 1000);
            });

        } else {
            Intent intent = new Intent(EyeMuscleTraining.this, Congratulations.class);
            startActivity(intent);
        }
    }
}