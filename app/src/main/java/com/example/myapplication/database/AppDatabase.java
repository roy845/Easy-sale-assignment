package com.example.myapplication.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.example.myapplication.constants.Constants;
import com.example.myapplication.converters.DateConverter;
import com.example.myapplication.database.dao.UserDao;
import com.example.myapplication.database.dao.UserSessionDao;
import com.example.myapplication.models.User;
import com.example.myapplication.models.UserSession;

@Database(entities = {User.class, UserSession.class}, exportSchema = false, version = 1)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    public static AppDatabase instance;
    private static final Object LOCK = new Object();

    public abstract UserDao userDao();
    public abstract UserSessionDao userSessionDao();

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, Constants.DATABASE_NAME)
                            .build();
                }
            }
        }

        return instance;
    }
}
