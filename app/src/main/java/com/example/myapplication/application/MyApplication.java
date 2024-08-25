package com.example.myapplication.application;

import android.app.Application;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import com.example.myapplication.viewmodel.UserSessionViewModel;
import java.util.Date;

public class MyApplication extends Application implements LifecycleObserver {

    private static MyApplication instance;
    private long sessionStartTime;
    private Date sessionDate;
    UserSessionViewModel userSessionViewModel;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        setupViewModel();

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    private void setupViewModel(){
        userSessionViewModel =  new ViewModelProvider.AndroidViewModelFactory(getInstance()).create(UserSessionViewModel.class);;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onAppForegrounded() {

        sessionStartTime = System.currentTimeMillis();
        sessionDate = new Date();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onAppBackgrounded() {

        long sessionEndTime = System.currentTimeMillis();
        long sessionDuration = sessionEndTime - sessionStartTime;

        userSessionViewModel.insertOrUpdateSession(sessionStartTime, sessionEndTime, sessionDuration, sessionDate);
    }

    public static MyApplication getInstance() {
        return instance;
    }
}

