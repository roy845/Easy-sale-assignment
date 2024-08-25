package com.example.myapplication.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.myapplication.models.DailyUsage;
import com.example.myapplication.models.UserSession;
import java.util.Date;
import java.util.List;

@Dao
public interface UserSessionDao {

    @Insert
    void insert(UserSession session);

    @Update
    void update(UserSession session);

    @Query("SELECT * FROM user_sessions WHERE sessionDate = :date LIMIT 1")
    UserSession getSessionByDate(Date date);

    @Query("SELECT strftime('%m-%d', datetime(sessionDate / 1000, 'unixepoch')) AS sessionDate, SUM(sessionDuration) as totalDuration " +
            "FROM user_sessions " +
            "WHERE sessionDate BETWEEN :startDate AND :endDate " +
            "GROUP BY sessionDate ORDER BY sessionDate ASC")
    LiveData<List<DailyUsage>> getDailyUsageForWeek(long startDate, long endDate);
}
