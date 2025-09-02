package com.eyecare;

import android.graphics.Path;

public class Utils {
    public static Path trianglePath(float x1, float y1, float x2, float y2, float x3, float y3) {
        Path p = new Path();
        p.moveTo(x1, y1);
        p.lineTo(x2, y2);
        p.lineTo(x3, y3);
        p.close();
        return p;
    }
}
