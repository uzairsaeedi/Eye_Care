package com.eyetest.eyecare.eyesighttest;

import android.os.Bundle;
import android.widget.ImageView;

public class PrivacyPolicyActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(v -> onBackPressed());
    }
}