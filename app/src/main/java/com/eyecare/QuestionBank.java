package com.eyecare;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionBank {

    public static List<Question> getQuestions(String exerciseType) {
        List<Question> list = new ArrayList<>();

        if ("snellen_chart".equalsIgnoreCase(exerciseType) || "contrast_sensitivity".equals(exerciseType)) {
            list.add(new Question("OFFED", "OFFED", "OFDEE", "OFEEE", "OFFED"));
            list.add(new Question("LPED", "LPED", "LEPD", "PLDE", "LPED"));
            list.add(new Question("ZQYTR", "ZQYTR", "ZQYRT", "ZQTR Y", "ZQYTR"));
            list.add(new Question("MTWQ", "MTWQ", "MWTQ", "TMWQ", "MTWQ"));
            list.add(new Question("ABCD", "ABCD", "ACBD", "ABDC", "ABCD"));
            list.add(new Question("XYZAB", "XYXAB", "XYZAB", "XYAAB", "XYZAB"));
        } else if ("color_blindness".equalsIgnoreCase(exerciseType)) {
            list.add(new Question(R.mipmap.ishihara_1_foreground, "7", "9", "1", "1"));
            list.add(new Question(R.mipmap.ishihara_2_foreground, "17", "12", "25", "12"));
            list.add(new Question(R.mipmap.ishihara_3_foreground, "5", "7", "3", "3"));
            list.add(new Question(R.mipmap.ishihara_4_foreground, "29", "70", "23", "29"));
            list.add(new Question(R.mipmap.ishihara_5_foreground, "13", "45", "16", "45"));
            list.add(new Question(R.mipmap.ishihara_6_foreground, "16", "25", "43", "16"));
        }
        else {
            list.add(new Question("OFFED", "OFFED", "OFDEE", "OFEEE", "OFFED"));
        }

        // Step 1: Randomize questions
        Collections.shuffle(list);

        // Step 2: Assign decreasing font sizes
        int startSize = 40;   // pehle question ka font size
        int decrement = 4;    // har next question me 4 point kam hoga
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setFontSize(startSize - (i * decrement));
        }

        return list;
    }

    public static int getTotalExercises() {
        String[] keys = new String[]{
                "snellen_chart",
                "color_blindness",
                "eye_muscle_training",
                "ocular_mobility",
                "color_test",
                "landolt_okn",
                "visual_activity",
                "astigmatism_test",
                "contrast_sensitivity"
        };
        return keys.length;
    }
}
