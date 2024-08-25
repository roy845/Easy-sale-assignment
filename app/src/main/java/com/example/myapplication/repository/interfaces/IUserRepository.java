package com.example.myapplication.repository.interfaces;

import androidx.lifecycle.LiveData;
import androidx.paging.PagingData;
import com.example.myapplication.models.User;
import com.example.myapplication.models.UserCountPerMonth;

import java.util.List;

public interface IUserRepository {
    LiveData<String> insertUser(User user);
    LiveData<String> insertAllUsers(List<User> users);
    LiveData<String> updateUser(User user);
    LiveData<String> deleteUser(User user);
    LiveData<String> deleteAllUsers();
    LiveData<List<UserCountPerMonth>> getUserCountPerMonth(long startDate, long endDate);
    LiveData<Integer> getTotalUserCount();
    LiveData<PagingData<User>> loadUsersByPage(int offset, int pageSize);
    LiveData<PagingData<User>> searchUsersWithPagination(String query,int offset,int limit);
}
