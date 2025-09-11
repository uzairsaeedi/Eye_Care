package com.eyetest.eyecare.eyesighttest;

import android.content.Context;
import android.content.SharedPreferences;

public class ProgressUtils {

    private static final String PREFS = "eyecare_prefs";
    private static final String KEY_OVERALL = "completed_tests_overall";
    private static final String KEY_DONE_ONCE_PREFIX = "done_once_";
    private static final String KEY_PER_EX_PREFIX = "completed_";

    public static void markTestCompleted(Context ctx, String exerciseType) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        int overall = prefs.getInt(KEY_OVERALL, 0);
        prefs.edit().putInt(KEY_OVERALL, overall + 1).apply();

        String perKey = KEY_PER_EX_PREFIX + exerciseType;
        int perCount = prefs.getInt(perKey, 0);
        prefs.edit().putInt(perKey, perCount + 1).apply();
    }

    public static void markTestCompletedUnique(Context ctx, String exerciseType) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String doneKey = KEY_DONE_ONCE_PREFIX + exerciseType;
        boolean done = prefs.getBoolean(doneKey, false);
        if (!done) {
            int overall = prefs.getInt(KEY_OVERALL, 0);
            prefs.edit().putInt(KEY_OVERALL, overall + 1).putBoolean(doneKey, true).apply();
        }
        String perKey = KEY_PER_EX_PREFIX + exerciseType;
        int perCount = prefs.getInt(perKey, 0);
        prefs.edit().putInt(perKey, perCount + 1).apply();
    }

    public static int getCompletedOverall(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_OVERALL, 0);
    }

    public static int getCompletedForExercise(Context ctx, String exerciseType) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String perKey = KEY_PER_EX_PREFIX + exerciseType;
        return prefs.getInt(perKey, 0);
    }

    public static int calculatePercent(int completed, int total) {
        if (total <= 0) return 0;
        double p = (completed / (double) total) * 100.0;
        int percent = (int) Math.round(p);
        if (percent > 100) percent = 100;
        if (percent < 0) percent = 0;
        return percent;
    }
}
