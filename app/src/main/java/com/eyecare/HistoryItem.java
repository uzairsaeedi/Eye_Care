package com.eyecare;

import java.util.List;

public class HistoryItem {
    private String date;
    private List<PerformanceItem> performanceList;

    public HistoryItem(String date, List<PerformanceItem> performanceList) {
        this.date = date;
        this.performanceList = performanceList;
    }

    public String getDate() { return date; }
    public List<PerformanceItem> getPerformanceList() { return performanceList; }
}
