package com.example.myapplication.models;

import androidx.room.ColumnInfo;

public class UserCountPerMonth {
    @ColumnInfo(name = "month")
    public String month;
    @ColumnInfo(name = "userCount")
    public int userCount;

    public UserCountPerMonth(String month, int userCount) {
        this.month = month;
        this.userCount = userCount;
    }

    public String getMonth() {
        return month;
    }

    public int getUserCount() {
        return userCount;
    }
}

