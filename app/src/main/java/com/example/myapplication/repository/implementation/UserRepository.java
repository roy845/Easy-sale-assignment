package com.example.myapplication.repository.implementation;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;
import com.example.myapplication.constants.Constants;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.models.User;
import com.example.myapplication.models.UserCountPerMonth;
import com.example.myapplication.repository.interfaces.IUserRepository;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class UserRepository implements IUserRepository {

    public AppDatabase appDatabase;
    private Executor executor = Executors.newSingleThreadExecutor();

    public UserRepository(Context context) {
        appDatabase = AppDatabase.getInstance(context);
    }

    @Override
    public LiveData<String> insertUser(User user) {
        MutableLiveData<String> result = new MutableLiveData<>();
        executor.execute(() -> {
            try {
                appDatabase.userDao().insertUser(user);
                result.postValue(Constants.SUCCESS);
            } catch (SQLiteConstraintException e) {
                result.postValue(Constants.CONSTRAINT_FAILURE);
            } catch (Exception e) {
                result.postValue(Constants.ERROR);
            }
        });

        return result;
    }
    @Override
    public void insertAllUsers(List<User> users) {
        executor.execute(() -> appDatabase.userDao().insertAllUsers(users));
    }
    @Override
    public LiveData<String> updateUser(User user) {
        MutableLiveData<String> result = new MutableLiveData<>();
        executor.execute(() -> {
            try {
                appDatabase.userDao().updateUser(user);
                result.postValue(Constants.SUCCESS);
            } catch (SQLiteConstraintException e) {
                result.postValue(Constants.CONSTRAINT_FAILURE);
            } catch (Exception e) {
                result.postValue(Constants.ERROR);
            }
        });

        return result;
    }
    @Override
    public LiveData<String> deleteUser(User user) {
        MutableLiveData<String> result = new MutableLiveData<>();
        executor.execute(() -> {
            try {
                int rowsDeleted = appDatabase.userDao().deleteUser(user);
                if (rowsDeleted > 0) {
                    result.postValue(Constants.SUCCESS);
                } else {
                    result.postValue(Constants.ERROR);
                }
            } catch (Exception e) {
                result.postValue(Constants.ERROR);
            }
        });

        return result;
    }

    @Override
    public LiveData<List<UserCountPerMonth>> getUserCountPerMonth(long startDate, long endDate) {
        return appDatabase.userDao().getUserCountPerMonth(startDate,endDate);
    }

    public LiveData<PagingData<User>> loadUsersByPage(int offset, int pageSize){
        return PagingLiveData.getLiveData(new Pager<>(
                new PagingConfig(
                        6,0,true
                ),
                () -> appDatabase.userDao().loadUsersByPage(offset,pageSize)
        ));
    }

    public LiveData<PagingData<User>> searchUsersWithPagination(String query, int offset, int limit){
        return PagingLiveData.getLiveData(new Pager<>(
                new PagingConfig(
                        6,0,true
                ),
                () -> appDatabase.userDao().searchUsersWithPagination("%" + query + "%",offset,limit)
        ));
    }

    public LiveData<Integer> getTotalUserCount(){
        return appDatabase.userDao().getTotalUserCount();
    }
}
