package com.eyetest.eyecare.eyesighttest;

public class Question {
    private String mainLetters;
    private String option1, option2, option3;
    private String correct;
    private int fontSize;
    private int imageResId = 0;

    public Question(String mainLetters, String option1, String option2, String option3, String correct) {
        this.mainLetters = mainLetters;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.correct = correct;
    }

    public Question(int imageResId, String option1, String option2, String option3, String correct) {
        this.imageResId = imageResId;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.correct = correct;
    }


    public String getMainLetters() { return mainLetters; }
    public String getOption1() { return option1; }
    public String getOption2() { return option2; }
    public String getOption3() { return option3; }
    public String getCorrect() { return correct; }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getImageResId() { return imageResId; }
    public void setImageResId(int res) { this.imageResId = res; }
}

