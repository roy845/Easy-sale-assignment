package com.example.myapplication.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagingData;
import com.example.myapplication.constants.Constants;
import com.example.myapplication.models.User;
import com.example.myapplication.models.UserCountPerMonth;
import com.example.myapplication.models.UserResponse;
import com.example.myapplication.network.ApiClient;
import com.example.myapplication.network.ApiService;
import com.example.myapplication.repository.implementation.UserRepository;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;


public class UserViewModel extends AndroidViewModel {

    private final UserRepository userRepository;
    private final ExecutorService executorService;
    private final MutableLiveData<Boolean> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loadingInitialUsersLiveData = new MutableLiveData<>();
    public LiveData<Boolean> getErrorLiveData() {
        return errorLiveData;
    }
    public LiveData<Boolean> getLoadingInitialUsers(){
        return loadingInitialUsersLiveData;
    }
    private final SharedPreferences sharedPreferences;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        this.executorService = Executors.newSingleThreadExecutor();
        this.sharedPreferences = application.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        checkAndFetchUsersFromApi();
    }


    public LiveData<List<UserCountPerMonth>> getUserCountPerMonth(long startDate, long endDate) {
        return userRepository.getUserCountPerMonth(startDate, endDate);
    }

    public void retryLoadUsers() {
        fetchAndStoreUsers(1);
    }

    public LiveData<String> insertUser(User user) {
        return userRepository.insertUser(user);
    }

    public void insertAllUsers(List<User> users) {
        userRepository.insertAllUsers(users);
    }

    public LiveData<String> updateUser(User user) {
        return userRepository.updateUser(user);
    }

    public LiveData<String> deleteUser(User user) {
       return userRepository.deleteUser(user);
    }

    public LiveData<String> deleteAllUsers() {
        return userRepository.deleteAllUsers();
    }

    public LiveData<PagingData<User>> loadUsersByPage(int offset, int pageSize) {
        return userRepository.loadUsersByPage(offset,pageSize);
    }

    public LiveData<PagingData<User>> searchUsersWithPagination(String query,int offset, int pageSize) {
        return userRepository.searchUsersWithPagination(query,offset,pageSize);
    }

    public LiveData<Integer> getTotalUserCount() {
        return userRepository.getTotalUserCount();
    }

    private void checkAndFetchUsersFromApi() {
        boolean hasFetchedUsers = sharedPreferences.getBoolean(Constants.PREF_FETCHED_USERS, false);
        if (!hasFetchedUsers) {
            fetchAndStoreUsers(1);
            errorLiveData.setValue(false);
        } else {
            errorLiveData.setValue(false);
            loadingInitialUsersLiveData.setValue(false);
        }
    }

    private void fetchAndStoreUsers(int pageNumber) {
        loadingInitialUsersLiveData.setValue(true);
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<UserResponse> call = apiService.getUsers(pageNumber);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<User> usersFromApi = response.body().getData();

                    Date now = new Date();
                    for (User user : usersFromApi) {
                        user.setCreatedAt(now);
                        user.setUpdatedAt(now);
                    }

                    executorService.execute(() -> insertAllUsers(usersFromApi));

                    if (pageNumber < response.body().getTotalPages()) {
                        fetchAndStoreUsers(pageNumber + 1);
                    } else {
                        loadingInitialUsersLiveData.setValue(false);
                        errorLiveData.setValue(false);
                        sharedPreferences.edit().putBoolean(Constants.PREF_FETCHED_USERS, true).apply();
                        Toast.makeText(getApplication().getApplicationContext(), Constants.USER_FETCHED_SUCCESSFULLY, Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                handleApiFailure(t);
                errorLiveData.setValue(true);
                loadingInitialUsersLiveData.setValue(false);
            }
        });
    }

    private void handleApiFailure(Throwable t) {
        String errorMessage;
        if (t instanceof IOException) {
            errorMessage = Constants.NETWORK_ERROR;
        } else if (t instanceof HttpException) {
            errorMessage = Constants.SERVER_ERROR;
        } else {
            errorMessage = Constants.UNEXPECTED_ERROR;
        }
        Toast.makeText(getApplication().getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
    }
}
