package com.example.myapplication.repository;

import androidx.lifecycle.LiveData;
import androidx.paging.PagingData;

import com.example.myapplication.models.User;

import java.util.List;

public interface IUserRepository {
    LiveData<String> insertUser(User user);
    void insertAllUsers(List<User> users);
    LiveData<String> updateUser(User user);
    LiveData<String> deleteUser(User user);
    LiveData<List<User>> getAllUsers();
    LiveData<List<User>> searchUsers(String query);
    LiveData<Integer> getUserCount();
    LiveData<PagingData<User>> getPagedUsers();
    LiveData<PagingData<User>> searchPagedUsers(String query);
}
