package com.example.myapplication.database.dao;


import androidx.lifecycle.LiveData;
import androidx.paging.PagingSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.myapplication.models.User;
import com.example.myapplication.models.UserCountPerMonth;

import java.util.List;

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

    @Query("DELETE FROM users")
    int deleteAllUsers();

    @Query("SELECT * FROM users WHERE LOWER(first_name) LIKE LOWER(:query) OR LOWER(last_name) LIKE LOWER(:query) ORDER BY LOWER(first_name) ASC LIMIT :limit OFFSET :offset")
    PagingSource<Integer, User> searchUsersWithPagination(String query, int offset,int limit);

    @Query("SELECT strftime('%Y-%m', datetime(createdAt / 1000, 'unixepoch')) AS month, " +
            "COUNT(*) AS userCount " +
            "FROM users " +
            "WHERE createdAt IS NOT NULL AND createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY month " +
            "ORDER BY month ASC")
    LiveData<List<UserCountPerMonth>> getUserCountPerMonth(long startDate, long endDate);

    @Query("SELECT COUNT(*) FROM users")
    LiveData<Integer> getTotalUserCount();

    @Query("SELECT * FROM users ORDER BY LOWER(first_name) ASC LIMIT :pageSize OFFSET :offset")
    PagingSource<Integer, User> loadUsersByPage(int offset, int pageSize);

}
