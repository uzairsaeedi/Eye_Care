package com.eyetest.eyecare.eyesighttest;

public class ListItem {
    private String title, subtitle;
    private int iconRes;
    public ListItem(String title, String subtitle, int iconRes) {
        this.title = title;
        this.iconRes = iconRes;
    }
    public String getTitle() { return title; }
    public int getIconRes() { return iconRes; }
}

