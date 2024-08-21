package com.example.myapplication.interfaces;

public interface DatabaseOperationCallback {
    void onSuccess();
    void onConstraintFailure();
    void onError(Exception e);
}
