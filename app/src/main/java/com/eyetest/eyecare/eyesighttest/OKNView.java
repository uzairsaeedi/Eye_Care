package com.eyetest.eyecare.eyesighttest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class OKNView extends View {

    private final Paint red = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint white = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float speedPxPerSec = 280f;

    private float stripeHeight = 60f;

    private float offset = 0f;

    private boolean running = true;
    private long lastFrameTimeNs = 0L;

    public OKNView(Context c) { super(c); init(); }
    public OKNView(Context c, AttributeSet a) { super(c, a); init(); }
    public OKNView(Context c, AttributeSet a, int s) { super(c, a, s); init(); }

    private void init() {
        red.setStyle(Paint.Style.FILL);
        red.setColor(0xFFFF0000);
        white.setStyle(Paint.Style.FILL);
        white.setColor(0xFFFFFFFF);
        setClickable(true);
    }

    public void setRunning(boolean run) {
        running = run;
        if (run) {
            lastFrameTimeNs = 0L;
            postInvalidateOnAnimation();
        }
    }

    public void setSpeed(float pxPerSec) {
        speedPxPerSec = Math.max(40f, Math.min(pxPerSec, 2000f));
    }

    public void setStripeHeight(float px) {
        stripeHeight = Math.max(20f, px);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final int w = getWidth();
        final int h = getHeight();

        // time-based smooth scrolling
        long now = System.nanoTime();
        if (lastFrameTimeNs != 0 && running) {
            float dt = (now - lastFrameTimeNs) / 1_000_000_000f;
            offset -= speedPxPerSec * dt;
            float period = stripeHeight * 2f;
            if (period > 0) {
                offset = ((offset % period) + period) % period;
            }
        }
        lastFrameTimeNs = now;

        float y = offset;
        while (y > 0) y -= stripeHeight;
        boolean drawRed = true;
        while (y < h) {
            float next = y + stripeHeight;
            canvas.drawRect(0, Math.max(0, y), w, Math.min(h, next), drawRed ? red : white);
            drawRed = !drawRed;
            y = next;
        }

        if (running) postInvalidateOnAnimation();
    }
}
