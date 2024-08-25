package com.example.myapplication.models;

import androidx.room.ColumnInfo;

public class DailyUsage {
    @ColumnInfo(name = "sessionDate")
    private String sessionDate;
    @ColumnInfo(name = "totalDuration")
    private long totalDuration;

    public DailyUsage(String sessionDate, long totalDuration) {
        this.sessionDate = sessionDate;
        this.totalDuration = totalDuration;
    }

    public String getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(String sessionDate) {
        this.sessionDate = sessionDate;
    }

    public long getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(long totalDuration) {
        this.totalDuration = totalDuration;
    }
}

