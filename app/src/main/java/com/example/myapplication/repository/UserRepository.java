package com.example.myapplication.repository;


import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
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

    public void deleteUser(User user) {
        executor.execute(() -> appDatabase.userDao().deleteUser(user));
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
}
