package com.eyetest.eyecare.eyesighttest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class PandaView extends View {

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int variant = 0;

    public PandaView(Context c) { super(c); init(); }
    public PandaView(Context c, @Nullable AttributeSet a) { super(c,a); init(); }
    public PandaView(Context c, @Nullable AttributeSet a, int s) { super(c,a,s); init(); }

    private void init() {
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
    }

    public void setVariant(int v) {
        variant = v;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        int size = Math.min(w, h);
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);

        int w = getWidth();
        int h = getHeight();
        float cx = w / 2f;
        float cy = h / 2f;
        float radius = Math.min(w, h) * 0.38f;

        paint.setColor(Color.WHITE);
        c.drawCircle(cx, cy, radius, paint);

        paint.setColor(Color.parseColor("#4D4D4D"));
        float earR = radius * 0.36f;
        c.drawCircle(cx - radius * 0.6f, cy - radius * 0.6f, earR, paint);
        c.drawCircle(cx + radius * 0.6f, cy - radius * 0.6f, earR, paint);

        float eyePatchW = radius * 0.48f;
        float eyePatchH = radius * 0.48f;
        paint.setColor(Color.parseColor("#2F2F2F"));
        c.drawCircle(cx - radius * 0.45f, cy - radius * 0.12f, eyePatchW/1.6f, paint);
        if (variant == 2) {
            paint.setColor(Color.parseColor("#1A1A1A"));
            c.drawCircle(cx + radius * 0.45f, cy - radius * 0.12f, eyePatchW/1.6f, paint);
        } else {
            paint.setColor(Color.parseColor("#2F2F2F"));
            c.drawCircle(cx + radius * 0.45f, cy - radius * 0.12f, eyePatchW/1.6f, paint);
        }

        float eyeR = radius * 0.12f;
        paint.setColor(Color.WHITE);
        if (variant == 1) {
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(Math.max(3, radius*0.06f));
            paint.setStyle(Paint.Style.STROKE);
            c.drawLine(cx - radius * 0.55f, cy - radius*0.11f, cx - radius * 0.35f, cy - radius*0.11f, paint);
            paint.setStyle(Paint.Style.FILL);
        } else {
            paint.setColor(Color.WHITE);
            c.drawCircle(cx - radius * 0.45f, cy - radius * 0.12f, eyeR, paint);
            paint.setColor(Color.BLACK);
            c.drawCircle(cx - radius * 0.45f, cy - radius * 0.12f, eyeR*0.45f, paint);
        }
        paint.setColor(Color.WHITE);
        c.drawCircle(cx + radius * 0.45f, cy - radius * 0.12f, eyeR, paint);
        paint.setColor(Color.BLACK);
        c.drawCircle(cx + radius * 0.45f, cy - radius * 0.12f, eyeR*0.45f, paint);

        paint.setColor(Color.parseColor("#2B2B2B"));
        c.drawCircle(cx, cy + radius*0.05f, radius*0.08f, paint);

        paint.setColor(Color.parseColor("#2B2B2B"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(Math.max(3, radius*0.06f));
        float mouthLeft = cx - radius*0.18f;
        float mouthRight = cx + radius*0.18f;
        float mouthY = cy + radius*0.25f;
        if (variant == 3) {
            c.drawLine(mouthLeft, mouthY, cx + radius*0.03f, mouthY + radius*0.06f, paint);
            c.drawLine(cx + radius*0.03f, mouthY + radius*0.06f, mouthRight, mouthY, paint);
        } else {
            c.drawArc(cx - radius*0.18f, mouthY - radius*0.05f, cx + radius*0.18f, mouthY + radius*0.18f, 0, 180, false, paint);
        }
        paint.setStyle(Paint.Style.FILL);
    }
}
