package com.eyecare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Congratulations extends BaseActivity {

    Button btnFinish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congratulations);

        btnFinish = findViewById(R.id.btnFinish);

        btnFinish.setOnClickListener(View ->{
            Intent intent = new Intent(Congratulations.this, MainActivity.class);
            startActivity(intent);
        });
    }
}