package com.eyetest.eyecare.eyesighttest;

import android.os.Bundle;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class OKNTestActivity extends BaseActivity {

    private OKNView oknView;
    private Button btnStartStop;
    private SeekBar sbSpeed;
    private TextView tvSpeed;
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okntest);

        oknView = findViewById(R.id.oknView);
        btnStartStop = findViewById(R.id.btnStartStop);
        sbSpeed = findViewById(R.id.sbSpeed);
        tvSpeed = findViewById(R.id.tvSpeed);
        ivBack = findViewById(R.id.ivBack);

        ivBack.setOnClickListener(v -> finish());

        oknView.setRunning(true);

        float stripeDp = 24f;
        float stripePx = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, stripeDp, getResources().getDisplayMetrics());
        oknView.setStripeHeight(stripePx);

        sbSpeed.setProgress(40);
        updateSpeedFromSeek();

        sbSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateSpeedFromSeek();
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        btnStartStop.setOnClickListener(v -> {
            if (btnStartStop.getText().toString().equalsIgnoreCase("Pause")) {
                oknView.setRunning(false);
                btnStartStop.setText("Start");
            } else {
                oknView.setRunning(true);
                btnStartStop.setText("Pause");
            }
        });
    }
    private void updateSpeedFromSeek() {
        int p = sbSpeed.getProgress();
        float speed = 80f + p * 7.2f;
        oknView.setSpeed(speed);
        tvSpeed.setText(String.valueOf((int) speed));
    }

    @Override
    protected void onPause() {
        super.onPause();
        oknView.setRunning(false);
        btnStartStop.setText("Start");
    }

    @Override
    protected void onResume() {
        super.onResume();
        oknView.setRunning(true);
        btnStartStop.setText("Pause");
    }
}