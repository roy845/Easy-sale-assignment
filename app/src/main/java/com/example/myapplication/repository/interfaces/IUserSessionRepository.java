package com.example.myapplication.repository.interfaces;

import androidx.lifecycle.LiveData;
import com.example.myapplication.models.DailyUsage;
import java.util.Date;
import java.util.List;

public interface IUserSessionRepository {
    void insertOrUpdateSession(long sessionStartTime, long sessionEndTime, long sessionDuration, Date sessionDate);
    LiveData<List<DailyUsage>> getDailyUsageForWeek(long startDate, long endDate);
}
