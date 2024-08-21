package com.example.myapplication.database;


import androidx.paging.PagingSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.models.User;

import java.util.List;

import kotlin.jvm.Throws;

@Dao
public interface UserDao {

    @Insert()
    void insertUser(User user);

    @Insert
    void insertAllUsers(List<User> users);

    @Update
    void updateUser(User user);

    @Delete
    int deleteUser(User user);

    @Query("SELECT * FROM users ORDER BY LOWER(first_name) ASC")
    PagingSource<Integer, User> getAllUsersPaging();

    @Query("SELECT * FROM users WHERE LOWER(first_name) LIKE LOWER(:query) OR LOWER(last_name) LIKE LOWER(:query) ORDER BY LOWER(first_name) ASC")
    PagingSource<Integer, User> searchUsersPaging(String query);
}
