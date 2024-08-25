package com.example.myapplication.repository.implementation;

import android.content.Context;
import androidx.lifecycle.LiveData;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.models.DailyUsage;
import com.example.myapplication.models.UserSession;
import com.example.myapplication.repository.interfaces.IUserSessionRepository;
import com.example.myapplication.utils.DateUtils;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UserSessionRepository implements IUserSessionRepository {
    public AppDatabase appDatabase;
    private Executor executorService;

    public UserSessionRepository(Context context) {
        appDatabase = AppDatabase.getInstance(context);
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insertOrUpdateSession(long sessionStartTime, long sessionEndTime, long sessionDuration, Date sessionDate) {

        executorService.execute(() -> {
            Date normalizedDate = DateUtils.normalizeToDay(sessionDate);

            UserSession existingSession = appDatabase.userSessionDao().getSessionByDate(normalizedDate);

            if (existingSession != null) {

                existingSession.setSessionEndTime(sessionEndTime);
                existingSession.setSessionDuration(existingSession.getSessionDuration() + sessionDuration);
                appDatabase.userSessionDao().update(existingSession);

            } else {

                UserSession newSession = new UserSession(sessionStartTime, sessionEndTime, sessionDuration, normalizedDate);
                appDatabase.userSessionDao().insert(newSession);

            }
        });
    }

    public LiveData<List<DailyUsage>> getDailyUsageForWeek(long startDate, long endDate) {
        return appDatabase.userSessionDao().getDailyUsageForWeek(startDate,endDate);
    }
}
