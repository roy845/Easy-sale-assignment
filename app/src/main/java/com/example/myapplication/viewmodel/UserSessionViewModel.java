package com.example.myapplication.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.myapplication.models.DailyUsage;
import com.example.myapplication.repository.implementation.UserSessionRepository;
import java.util.Date;
import java.util.List;

public class UserSessionViewModel extends AndroidViewModel {

    private final UserSessionRepository repository;

    public UserSessionViewModel(@NonNull Application application) {
        super(application);
        repository = new UserSessionRepository(application);
    }

    public void insertOrUpdateSession(long sessionStartTime, long sessionEndTime, long sessionDuration, Date sessionDate) {
        repository.insertOrUpdateSession(sessionStartTime, sessionEndTime, sessionDuration, sessionDate);
    }

    public LiveData<List<DailyUsage>> getDailyUsageForWeek(long startDate, long endDate) {
        return repository.getDailyUsageForWeek(startDate, endDate);
    }
}

