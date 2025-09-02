package com.eyecare;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * Generic simple cartoon animal drawer.
 * animalType: 0 = Panda, 1 = Cat, 2 = Dog, 3 = Bunny, 4 = Fox
 * variant: 0 = normal, 1 = wink/eye-closed, 2 = patch/darker eye, 3 = tilted mouth
 *
 * This is intentionally simple vector drawing so you don't need external assets.
 */
public class AnimalView extends View {

    private final Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int animalType = 0;
    private int variant = 0;

    public AnimalView(Context c) { super(c); init(); }
    public AnimalView(Context c, @Nullable AttributeSet a) { super(c,a); init(); }
    public AnimalView(Context c, @Nullable AttributeSet a, int s) { super(c,a,s); init(); }

    private void init() {
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.BLACK);
    }

    public void setAnimalType(int t) {
        animalType = Math.max(0, Math.min(t, 4));
        invalidate();
    }

    public void setVariant(int v) {
        variant = Math.max(0, Math.min(v, 3));
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // keep square cell
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
        float R = Math.min(w, h) * 0.42f; // head radius

        // clear background (transparent)
        // draw animal according to type
        switch (animalType) {
            case 0: drawPanda(c, cx, cy, R); break;
            case 1: drawCat(c, cx, cy, R); break;
            case 2: drawDog(c, cx, cy, R); break;
            case 3: drawBunny(c, cx, cy, R); break;
            case 4: drawFox(c, cx, cy, R); break;
            default: drawPanda(c, cx, cy, R); break;
        }
    }

    // ---------- helpers to draw animals (simple stylized shapes) ------------

    private void drawPanda(Canvas c, float cx, float cy, float r) {
        // head
        p.setColor(Color.WHITE); c.drawCircle(cx, cy, r, p);
        // ears
        p.setColor(Color.DKGRAY);
        c.drawCircle(cx - r*0.6f, cy - r*0.6f, r*0.28f, p);
        c.drawCircle(cx + r*0.6f, cy - r*0.6f, r*0.28f, p);

        // eye patches
        p.setColor(Color.DKGRAY);
        c.drawCircle(cx - r*0.45f, cy - r*0.12f, r*0.22f, p);
        c.drawCircle(cx + r*0.45f, cy - r*0.12f, r*0.22f, p);

        // eyes (variant handling)
        if (variant == 1) { // wink - left closed
            p.setColor(Color.BLACK); p.setStrokeWidth(Math.max(3, r*0.06f)); p.setStyle(Paint.Style.STROKE);
            c.drawLine(cx - r*0.6f, cy - r*0.12f, cx - r*0.3f, cy - r*0.12f, p);
            p.setStyle(Paint.Style.FILL); c.drawCircle(cx + r*0.45f, cy - r*0.12f, r*0.08f, p);
        } else {
            p.setColor(Color.WHITE); c.drawCircle(cx - r*0.45f, cy - r*0.12f, r*0.08f, p);
            p.setColor(Color.BLACK); c.drawCircle(cx - r*0.45f, cy - r*0.12f, r*0.04f, p);
            p.setColor(Color.WHITE); c.drawCircle(cx + r*0.45f, cy - r*0.12f, r*0.08f, p);
            p.setColor(Color.BLACK); c.drawCircle(cx + r*0.45f, cy - r*0.12f, r*0.04f, p);
        }

        if (variant == 2) { // patch darker right
            p.setColor(Color.BLACK); c.drawCircle(cx + r*0.45f, cy - r*0.12f, r*0.22f, p);
        }

        // nose & mouth
        p.setColor(Color.BLACK);
        c.drawCircle(cx, cy + r*0.05f, r*0.06f, p);
        if (variant == 3) {
            // tilted mouth
            p.setStrokeWidth(r*0.05f); p.setStyle(Paint.Style.STROKE);
            c.drawLine(cx - r*0.14f, cy + r*0.25f, cx + r*0.02f, cy + r*0.33f, p);
            c.drawLine(cx + r*0.02f, cy + r*0.33f, cx + r*0.18f, cy + r*0.26f, p);
            p.setStyle(Paint.Style.FILL);
        } else {
            p.setStyle(Paint.Style.STROKE); p.setStrokeWidth(r*0.05f);
            c.drawArc(cx - r*0.18f, cy + r*0.16f, cx + r*0.18f, cy + r*0.40f, 0, 180, false, p);
            p.setStyle(Paint.Style.FILL);
        }
    }

    private void drawCat(Canvas c, float cx, float cy, float r) {
        // head round
        p.setColor(Color.parseColor("#FFF6E3")); c.drawCircle(cx, cy, r, p);
        // ears triangles
        p.setColor(Color.parseColor("#D89E6A"));
        float ex = r*0.7f, ey = r*0.7f;
        // left
        c.drawPath(Utils.trianglePath(cx - ex, cy - ey, cx - ex + r*0.35f, cy - ey - r*0.25f, cx - ex + r*0.7f, cy - ey), p);
        // right
        c.drawPath(Utils.trianglePath(cx + ex, cy - ey, cx + ex - r*0.35f, cy - ey - r*0.25f, cx + ex - r*0.7f, cy - ey), p);

        // eyes - variants
        if (variant == 1) {
            // wink left
            p.setColor(Color.BLACK); p.setStrokeWidth(r*0.06f); p.setStyle(Paint.Style.STROKE);
            c.drawLine(cx - r*0.35f, cy - r*0.08f, cx - r*0.15f, cy - r*0.08f, p);
            p.setStyle(Paint.Style.FILL); c.drawCircle(cx + r*0.25f, cy - r*0.12f, r*0.06f, p);
        } else {
            p.setColor(Color.BLACK); c.drawCircle(cx - r*0.25f, cy - r*0.12f, r*0.06f, p);
            c.drawCircle(cx + r*0.25f, cy - r*0.12f, r*0.06f, p);
        }

        if (variant == 2) {
            // eye patch right darker
            p.setColor(Color.parseColor("#7A4B3A")); c.drawCircle(cx + r*0.25f, cy - r*0.12f, r*0.14f, p);
            p.setColor(Color.WHITE); c.drawCircle(cx + r*0.25f, cy - r*0.12f, r*0.06f, p);
            p.setColor(Color.BLACK); c.drawCircle(cx + r*0.25f, cy - r*0.12f, r*0.03f, p);
        }

        // nose & mouth
        p.setColor(Color.BLACK);
        c.drawCircle(cx, cy + r*0.02f, r*0.05f, p);
        if (variant == 3) {
            p.setStrokeWidth(r*0.04f); p.setStyle(Paint.Style.STROKE);
            c.drawLine(cx, cy + r*0.05f, cx + r*0.14f, cy + r*0.18f, p);
            c.drawLine(cx, cy + r*0.05f, cx - r*0.14f, cy + r*0.18f, p);
            p.setStyle(Paint.Style.FILL);
        } else {
            p.setStyle(Paint.Style.STROKE); p.setStrokeWidth(r*0.04f);
            c.drawArc(cx - r*0.12f, cy + r*0.05f, cx + r*0.12f, cy + r*0.22f, 0, 180, false, p);
            p.setStyle(Paint.Style.FILL);
        }
    }

    private void drawDog(Canvas c, float cx, float cy, float r) {
        // base head slightly tan
        p.setColor(Color.parseColor("#FFDDBA")); c.drawCircle(cx, cy, r, p);
        // ears as rounded rectangles (left/right)
        p.setColor(Color.parseColor("#C68A4E"));
        c.drawRoundRect(cx - r - r*0.05f, cy - r*0.2f, cx - r*0.2f, cy + r*0.45f, r*0.3f, r*0.3f, p);
        c.drawRoundRect(cx + r*0.2f, cy - r*0.2f, cx + r + r*0.05f, cy + r*0.45f, r*0.3f, r*0.3f, p);

        // eyes
        p.setColor(Color.BLACK);
        if (variant == 1) { // wink left
            p.setStrokeWidth(r*0.06f); p.setStyle(Paint.Style.STROKE);
            c.drawLine(cx - r*0.35f, cy - r*0.12f, cx - r*0.15f, cy - r*0.12f, p);
            p.setStyle(Paint.Style.FILL); c.drawCircle(cx + r*0.25f, cy - r*0.12f, r*0.06f, p);
        } else {
            c.drawCircle(cx - r*0.25f, cy - r*0.12f, r*0.06f, p);
            c.drawCircle(cx + r*0.25f, cy - r*0.12f, r*0.06f, p);
        }

        if (variant == 2) {
            p.setColor(Color.rgb(80,40,20));
            c.drawCircle(cx + r*0.25f, cy - r*0.12f, r*0.18f, p);
            p.setColor(Color.BLACK); c.drawCircle(cx + r*0.25f, cy - r*0.12f, r*0.06f, p);
        }

        // nose
        p.setColor(Color.BLACK); c.drawOval(cx - r*0.06f, cy + r*0.02f, cx + r*0.06f, cy + r*0.06f, p);
        // mouth
        p.setStyle(Paint.Style.STROKE); p.setStrokeWidth(r*0.04f);
        c.drawArc(cx - r*0.12f, cy + r*0.07f, cx + r*0.12f, cy + r*0.22f, 0, 180, false, p);
        p.setStyle(Paint.Style.FILL);
    }

    private void drawBunny(Canvas c, float cx, float cy, float r) {
        // big ears
        p.setColor(Color.WHITE);
        c.drawOval(cx - r*0.45f, cy - r*1.05f, cx - r*0.15f, cy + r*0.15f, p);
        c.drawOval(cx + r*0.15f, cy - r*1.05f, cx + r*0.45f, cy + r*0.15f, p);
        // head
        p.setColor(Color.WHITE); c.drawCircle(cx, cy, r, p);
        p.setColor(Color.BLACK);
        if (variant == 1) {
            p.setStyle(Paint.Style.STROKE); p.setStrokeWidth(r*0.06f);
            c.drawLine(cx - r*0.35f, cy - r*0.12f, cx - r*0.15f, cy - r*0.12f, p);
            p.setStyle(Paint.Style.FILL);
            c.drawCircle(cx + r*0.25f, cy - r*0.12f, r*0.06f, p);
        } else {
            c.drawCircle(cx - r*0.25f, cy - r*0.12f, r*0.06f, p);
            c.drawCircle(cx + r*0.25f, cy - r*0.12f, r*0.06f, p);
        }
        // nose
        p.setColor(Color.rgb(200,80,120)); c.drawCircle(cx, cy + r*0.02f, r*0.05f, p);
        // mouth
        p.setColor(Color.BLACK); p.setStyle(Paint.Style.STROKE); p.setStrokeWidth(r*0.035f);
        c.drawArc(cx - r*0.09f, cy + r*0.07f, cx + r*0.09f, cy + r*0.15f, 0, 180, false, p);
        p.setStyle(Paint.Style.FILL);
    }

    private void drawFox(Canvas c, float cx, float cy, float r) {
        // orange head
        p.setColor(Color.parseColor("#FF8C3A")); c.drawCircle(cx, cy, r, p);
        // ears (white inside)
        p.setColor(Color.parseColor("#FF8C3A"));
        c.drawPath(Utils.trianglePath(cx - r*0.6f, cy - r*0.6f, cx - r*0.35f, cy - r*0.95f, cx - r*0.1f, cy - r*0.6f), p);
        c.drawPath(Utils.trianglePath(cx + r*0.6f, cy - r*0.6f, cx + r*0.35f, cy - r*0.95f, cx + r*0.1f, cy - r*0.6f), p);
        p.setColor(Color.WHITE);
        c.drawCircle(cx - r*0.25f, cy + r*0.02f, r*0.12f, p);
        c.drawCircle(cx + r*0.25f, cy + r*0.02f, r*0.12f, p);
        p.setColor(Color.BLACK);
        if (variant == 1) {
            p.setStrokeWidth(r*0.06f); p.setStyle(Paint.Style.STROKE);
            c.drawLine(cx - r*0.32f, cy + r*0.02f, cx - r*0.18f, cy + r*0.02f, p);
            p.setStyle(Paint.Style.FILL);
            c.drawCircle(cx + r*0.25f, cy + r*0.02f, r*0.04f, p);
        } else {
            c.drawCircle(cx - r*0.25f, cy + r*0.02f, r*0.04f, p);
            c.drawCircle(cx + r*0.25f, cy + r*0.02f, r*0.04f, p);
        }
        // nose
        p.setColor(Color.BLACK); c.drawCircle(cx, cy + r*0.06f, r*0.05f, p);
        // mouth
        p.setStyle(Paint.Style.STROKE); p.setStrokeWidth(r*0.04f);
        c.drawArc(cx - r*0.12f, cy + r*0.12f, cx + r*0.12f, cy + r*0.24f, 0, 180, false, p);
        p.setStyle(Paint.Style.FILL);
    }
}
