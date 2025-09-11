package com.eyetest.eyecare.eyesighttest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

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