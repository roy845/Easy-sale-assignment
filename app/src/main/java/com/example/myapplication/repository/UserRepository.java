package com.example.myapplication.repository;


import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;

import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.models.User;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class UserRepository implements IUserRepository {

    private AppDatabase appDatabase;
    private Executor executor = Executors.newSingleThreadExecutor();

    public UserRepository(Context context) {
        appDatabase = AppDatabase.getInstance(context);
    }

    public LiveData<String> insertUser(User user) {
        MutableLiveData<String> result = new MutableLiveData<>();
        executor.execute(() -> {
            try {
                appDatabase.userDao().insertUser(user);
                result.postValue("success");
            } catch (SQLiteConstraintException e) {
                result.postValue("constraint_failure");
            } catch (Exception e) {
                result.postValue("error");
            }
        });

        return result;
    }

    public void insertAllUsers(List<User> users) {
        executor.execute(() -> appDatabase.userDao().insertAllUsers(users));
    }

    public LiveData<String> updateUser(User user) {
        MutableLiveData<String> result = new MutableLiveData<>();
        executor.execute(() -> {
            try {
                appDatabase.userDao().updateUser(user);
                result.postValue("success");
            } catch (SQLiteConstraintException e) {
                result.postValue("constraint_failure");
            } catch (Exception e) {
                result.postValue("error");
            }
        });

        return result;
    }

    public LiveData<String> deleteUser(User user) {
        MutableLiveData<String> result = new MutableLiveData<>();
        executor.execute(() -> {
            try {
                int rowsDeleted = appDatabase.userDao().deleteUser(user);
                if (rowsDeleted > 0) {
                    result.postValue("success");
                } else {
                    result.postValue("error");
                }
            } catch (Exception e) {
                result.postValue("error");
            }
        });

        return result;
    }

    public LiveData<List<User>> getAllUsers()  {
        return appDatabase.userDao().getAllUsers();
    }

    public LiveData<List<User>> searchUsers(String query)  {
        return appDatabase.userDao().searchUsers("%" + query + "%");
    }

    public LiveData<Integer> getUserCount() {
      return appDatabase.userDao().getUserCount();
    }


    public LiveData<PagingData<User>> getPagedUsers() {
        return PagingLiveData.getLiveData(new Pager<>(
                new PagingConfig(
                        2,0,true
                ),
                () -> appDatabase.userDao().getAllUsersPaging()
        ));
    }

    public LiveData<PagingData<User>> searchPagedUsers(String query) {
        return PagingLiveData.getLiveData(new Pager<>(
                new PagingConfig(
                        2,0,true
                ),
                () -> appDatabase.userDao().searchUsersPaging("%" + query + "%")
        ));
    }
}
