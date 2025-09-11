package com.eyetest.eyecare.eyesighttest;

public class PerformanceItem {
    private String title, subtitle;
    private int percent;
    public PerformanceItem(String title, String subtitle, int percent){
        this.title=title; this.subtitle=subtitle; this.percent=percent;
    }
    public String getTitle(){return title;}
    public String getSubtitle(){return subtitle;}
    public int getPercent(){return percent;}
}


