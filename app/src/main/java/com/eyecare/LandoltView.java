package com.eyecare;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class LandoltView extends View {

    private Paint paintC;
    private float angle = 0f;

    public LandoltView(Context context) {
        super(context);
        init();
    }

    public LandoltView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LandoltView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paintC = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintC.setStyle(Paint.Style.STROKE);
        paintC.setColor(0xFF000000);
        paintC.setStrokeWidth(30f);
    }

    public void setAngle(float degrees) {
        this.angle = degrees;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w = getWidth();
        int h = getHeight();
        float r = Math.min(w, h) / 4f;

        canvas.save();
        canvas.translate(w/2f, h/2f);
        canvas.rotate(angle);

        // Draw "C" (arc from 45° to 315°)
        Path path = new Path();
        path.addArc(-r, -r, r, r, 45, 270);
        canvas.drawPath(path, paintC);

        canvas.restore();
    }
}
