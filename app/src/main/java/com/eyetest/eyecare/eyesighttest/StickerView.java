package com.eyetest.eyecare.eyesighttest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class StickerView extends androidx.appcompat.widget.AppCompatImageView {

    private float dX, dY;
    private float scale = 1f, rotation = 0f;

    // simple multi-touch handling
    private static final int NONE = 0, DRAG = 1, ZOOM = 2;
    private int mode = NONE;
    private float startDist = 0f;
    private float startRotation = 0f;
    private float midX, midY;

    public StickerView(Context c) { super(c); init(); }
    public StickerView(Context c, AttributeSet a) { super(c,a); init(); }
    public StickerView(Context c, AttributeSet a, int d) { super(c,a,d); init(); }

    private void init() {
        setScaleType(ScaleType.MATRIX);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mode = DRAG;
                dX = getX() - event.getRawX();
                dY = getY() - event.getRawY();
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                mode = ZOOM;
                startDist = distance(event);
                startRotation = rotation(event);
                midX = (event.getX(0) + event.getX(1)) / 2f;
                midY = (event.getY(0) + event.getY(1)) / 2f;
                break;

            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    setX(event.getRawX() + dX);
                    setY(event.getRawY() + dY);
                } else if (mode == ZOOM && event.getPointerCount() >= 2) {
                    float newDist = distance(event);
                    float scaleFactor = newDist / startDist;
                    scale *= scaleFactor;
                    // rotation
                    float newRot = rotation(event);
                    float rotDelta = newRot - startRotation;
                    rotation += rotDelta;

                    // apply transforms
                    setPivotX(getWidth()/2f);
                    setPivotY(getHeight()/2f);
                    setScaleX(scale);
                    setScaleY(scale);
                    setRotation(rotation);

                    // reset baseline
                    startDist = newDist;
                    startRotation = newRot;
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
        }
        return true;
    }

    private float distance(MotionEvent e) {
        if (e.getPointerCount() < 2) return 0f;
        float dx = e.getX(0) - e.getX(1);
        float dy = e.getY(0) - e.getY(1);
        return (float) Math.hypot(dx, dy);
    }

    private float rotation(MotionEvent e) {
        if (e.getPointerCount() < 2) return 0f;
        double dx = e.getX(1) - e.getX(0);
        double dy = e.getY(1) - e.getY(0);
        return (float) Math.toDegrees(Math.atan2(dy, dx));
    }
}
