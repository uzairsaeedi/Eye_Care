package com.eyecare;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class LandoltActivity extends BaseActivity {

    private LandoltView landoltView;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landolt);

        landoltView = findViewById(R.id.landoltC);

        View btnTop = findViewById(R.id.btnTop);
        View btnBottom = findViewById(R.id.btnBottom);
        View btnLeft = findViewById(R.id.btnLeft);
        View btnRight = findViewById(R.id.btnRight);
        View btnTopLeft = findViewById(R.id.btnTopLeft);
        View btnTopRight = findViewById(R.id.btnTopRight);
        View btnBottomLeft = findViewById(R.id.btnBottomLeft);
        View btnBottomRight = findViewById(R.id.btnBottomRight);

        btnTop.setBackgroundResource(R.drawable.box_arrow_down);
        btnBottom.setBackgroundResource(R.drawable.box_arrow_up);
        btnLeft.setBackgroundResource(R.drawable.box_arrow_right);
        btnRight.setBackgroundResource(R.drawable.box_arrow_left);

        btnTopLeft.setBackgroundResource(R.drawable.box_arrow_135);
        btnTopRight.setBackgroundResource(R.drawable.box_arrow_225);
        btnBottomLeft.setBackgroundResource(R.drawable.box_arrow_45);
        btnBottomRight.setBackgroundResource(R.drawable.box_arrow_315);

        findViewById(R.id.btnTop).setOnClickListener(v -> landoltView.setAngle(-270));
        findViewById(R.id.btnRight).setOnClickListener(v -> landoltView.setAngle(-180));
        findViewById(R.id.btnBottom).setOnClickListener(v -> landoltView.setAngle(-90));
        findViewById(R.id.btnLeft).setOnClickListener(v -> landoltView.setAngle(-0));

        findViewById(R.id.btnTopRight).setOnClickListener(v -> landoltView.setAngle(-225));
        findViewById(R.id.btnBottomRight).setOnClickListener(v -> landoltView.setAngle(-135));
        findViewById(R.id.btnBottomLeft).setOnClickListener(v -> landoltView.setAngle(-45));
        findViewById(R.id.btnTopLeft).setOnClickListener(v -> landoltView.setAngle(-315));

        ivBack = findViewById(R.id.ivBack);

        ivBack.setOnClickListener(v -> finish());

    }
}
