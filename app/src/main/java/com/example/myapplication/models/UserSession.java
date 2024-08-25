package com.example.myapplication.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "user_sessions")
public class UserSession {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private long sessionStartTime;
    private long sessionEndTime;
    private long sessionDuration;
    private Date sessionDate;

    public UserSession(long sessionStartTime, long sessionEndTime, long sessionDuration, Date sessionDate) {
        this.sessionStartTime = sessionStartTime;
        this.sessionEndTime = sessionEndTime;
        this.sessionDuration = sessionDuration;
        this.sessionDate = sessionDate;
    }

    @NonNull
    @Override
    public String toString() {
        return "UserSession{" +
                "id=" + id +
                ", sessionStartTime=" + sessionStartTime +
                ", sessionEndTime=" + sessionEndTime +
                ", sessionDuration=" + sessionDuration +
                ", sessionDate=" + sessionDate +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getSessionStartTime() {
        return sessionStartTime;
    }

    public void setSessionStartTime(long sessionStartTime) {
        this.sessionStartTime = sessionStartTime;
    }

    public long getSessionEndTime() {
        return sessionEndTime;
    }

    public void setSessionEndTime(long sessionEndTime) {
        this.sessionEndTime = sessionEndTime;
    }

    public long getSessionDuration() {
        return sessionDuration;
    }

    public void setSessionDuration(long sessionDuration) {
        this.sessionDuration = sessionDuration;
    }

    public Date getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(Date sessionDate) {
        this.sessionDate = sessionDate;
    }
}

