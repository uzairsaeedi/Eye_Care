package com.eyetest.eyecare.eyesighttest;

import android.app.AlertDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    protected boolean isHomeActivity() {
        return false;
    }

    @Override
    public void onBackPressed() {
        if (!isHomeActivity()) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
            return;
        }
        showExitConfirmation();
    }

    protected void showExitConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Exit")
                .setMessage("You are about to exit the app. Are you sure?")
                .setCancelable(true)
                .setPositiveButton("Yes", (dialog, which) -> finishAffinity())
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
