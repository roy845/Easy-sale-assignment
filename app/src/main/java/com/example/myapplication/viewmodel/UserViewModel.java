package com.example.myapplication.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.myapplication.models.User;
import com.example.myapplication.models.UserResponse;
import com.example.myapplication.network.ApiClient;
import com.example.myapplication.network.ApiService;
import com.example.myapplication.repository.UserRepository;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;
import androidx.paging.PagingData;

public class UserViewModel extends AndroidViewModel {

    private static UserViewModel instance;
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
    private static final String PREF_FETCHED_USERS = "pref_fetched_users";
    private final SharedPreferences sharedPreferences;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        this.executorService = Executors.newSingleThreadExecutor();
        this.sharedPreferences = application.getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
        checkAndFetchUsersFromApi();
    }

    public static UserViewModel getInstance(Application application) {
        if (instance == null) {
            instance = new UserViewModel(application);
        }
        return instance;
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

    public LiveData<PagingData<User>> getPagedUsers() {
        return userRepository.getPagedUsers();
    }

    public LiveData<PagingData<User>> searchPagedUsers(String query) {
        return userRepository.searchPagedUsers(query);
    }

    private void checkAndFetchUsersFromApi() {
        boolean hasFetchedUsers = sharedPreferences.getBoolean(PREF_FETCHED_USERS, false);
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

                    executorService.execute(() -> {
                        insertAllUsers(usersFromApi);
                    });

                        if (pageNumber < response.body().getTotalPages()) {
                            fetchAndStoreUsers(pageNumber + 1);
                        } else {
                            loadingInitialUsersLiveData.setValue(false);
                            errorLiveData.setValue(false); // Notify LiveData that data is ready
                            sharedPreferences.edit().putBoolean(PREF_FETCHED_USERS, true).apply();
                            Toast.makeText(getApplication().getApplicationContext(), "Users fetched successfully", Toast.LENGTH_LONG).show();
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
            errorMessage = "Network error, please check your connection";
        } else if (t instanceof HttpException) {
            errorMessage = "Server error, please try again later";
        } else {
            errorMessage = "Unknown error occurred";
        }
        Toast.makeText(getApplication().getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
    }
}
