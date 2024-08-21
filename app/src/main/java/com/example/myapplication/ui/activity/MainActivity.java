package com.example.myapplication.ui.activity;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.LoadState;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.adapter.SwipeToDeleteCallback;
import com.example.myapplication.adapter.UserAdapter;
import com.example.myapplication.interfaces.OnClickUserInterface;
import com.example.myapplication.models.User;
import com.example.myapplication.viewmodel.UserViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import android.net.Uri;



public class MainActivity extends AppCompatActivity implements OnClickUserInterface {
    private UserViewModel userViewModel;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    ProgressBar progressBar,progressBarLoadingInitialUsers;
    EditText searchEditText;
    FloatingActionButton floatingActionButton;
    private final Handler handler = new Handler();
    private Runnable searchRunnable;
    private TextView emptyResultsTextView;
    private ImageView emptyResultsImageView;
    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String PREF_DONT_SHOW_AGAIN = "dont_show_again";
    private boolean isDialogShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {

            actionBar.setTitle("");

            LinearLayout customActionBarView = createCustomActionBarView();


            ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    Gravity.END | Gravity.CENTER_VERTICAL
            );

            actionBar.setCustomView(customActionBarView, params);
            actionBar.setDisplayShowCustomEnabled(true);
        }

        initViews();
        setupViewModel();
        observePagedUsers();
        setupEditTextSearch();
        observeErrorLiveData();
        enableSwipeToDeleteAndUndo();
        observeInitialLoadingLiveData();
        navigateToAddUserActivity();
    }


    private void observeInitialLoadingLiveData(){
        userViewModel.getLoadingInitialUsers().observe(MainActivity.this,isLoadingUsers->{
            if(isLoadingUsers){
                progressBarLoadingInitialUsers.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }else{
                progressBarLoadingInitialUsers.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
    }

    public void observeErrorLiveData(){
        userViewModel.getErrorLiveData().observe(this, error -> {
            System.out.println("error : "+error);
            if(error){
                navigateToErrorActivity();
            }else {
                checkAndShowWelcomeDialog();
            }
        });
    }

    private void  navigateToErrorActivity(){
        startActivity(new Intent(MainActivity.this, ErrorLoadingUsers.class));
    }

    private boolean shouldShowWelcomeDialog() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return !sharedPreferences.getBoolean(PREF_DONT_SHOW_AGAIN, false);
    }

    private void checkAndShowWelcomeDialog() {
        if (!isDialogShown && shouldShowWelcomeDialog()) {
            showWelcomeDialog();
            isDialogShown = true;
        }
    }

    private void showWelcomeDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_welcome, null);

        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        CheckBox checkboxDontShow = dialogView.findViewById(R.id.checkbox_dont_show);
        Button btnOk = dialogView.findViewById(R.id.btn_ok);
        Button learnMoreBtn = dialogView.findViewById(R.id.btn_learn_more);
        Button letsGoBtn = dialogView.findViewById(R.id.btn_lets_go);

        btnOk.setOnClickListener(v -> {

            if (checkboxDontShow.isChecked()) {
                SharedPreferences.Editor editor = this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
                editor.putBoolean(PREF_DONT_SHOW_AGAIN, true);
                editor.apply();
            }

            alertDialog.dismiss();
        });

        letsGoBtn.setOnClickListener(v -> {

            if (checkboxDontShow.isChecked()) {
                SharedPreferences.Editor editor = this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
                editor.putBoolean(PREF_DONT_SHOW_AGAIN, true);
                editor.apply();
            }
            alertDialog.dismiss();
        });

        learnMoreBtn.setOnClickListener(v -> {

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://easy-sale.co.il/"));
            startActivity(browserIntent);
            alertDialog.dismiss();
        });

        alertDialog.show();
    }

    private void initViews(){
        floatingActionButton = findViewById(R.id.fab_add_user);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter(UserAdapter.DIFF_CALLBACK,this);
        recyclerView.setAdapter(userAdapter);
        searchEditText = findViewById(R.id.editSearch);
        progressBar = findViewById(R.id.progressBarId);
        progressBarLoadingInitialUsers = findViewById(R.id.progressBarLoadingInitialUsers);
        emptyResultsImageView = findViewById(R.id.empty_results_image);
        emptyResultsTextView = findViewById(R.id.empty_results_text);
    }

    private void setupViewModel() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    private void observePagedUsers(){
        userViewModel.getPagedUsers().observe(this, usersList -> {
            userAdapter.submitData(getLifecycle(),usersList);
            handleEmptyResults();

        });
    }

    private LinearLayout createCustomActionBarView() {

        TextView textView = new TextView(this);
        textView.setText(R.string.app_name);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(18);
        textView.setGravity(Gravity.END);


        ImageView imageView = getImageView();

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        linearLayout.addView(imageView);
        linearLayout.addView(textView);

        return linearLayout;
    }

    private @NonNull ImageView getImageView() {
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.baseline_people_24);

        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        imageParams.setMargins(16, 0, 0, 0);

        imageView.setLayoutParams(imageParams);
        return imageView;
    }


    private void setupEditTextSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                progressBar.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                emptyResultsTextView.setVisibility(View.GONE);
                emptyResultsImageView.setVisibility(View.GONE);


                if (searchRunnable != null) {
                    handler.removeCallbacks(searchRunnable);
                }

                searchRunnable = () -> {
                    if (s.toString().trim().isEmpty()) {
                        observePagedUsers();
                    } else {
                        searchUsers(s.toString());
                    }
                };

                handler.postDelayed(searchRunnable, 500);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void handleEmptyResults() {
        userAdapter.addLoadStateListener(loadStates -> {
            if (loadStates.getRefresh() instanceof LoadState.NotLoading && userAdapter.getItemCount() == 0) {
                showNoResults();
            } else {
                showResults();
            }
            progressBar.setVisibility(View.GONE);
            return null;
        });
    }

    private void showResults() {
        recyclerView.setVisibility(View.VISIBLE);
        emptyResultsImageView.setVisibility(View.GONE);
        emptyResultsTextView.setVisibility(View.GONE);
    }

    private void showNoResults() {
        recyclerView.setVisibility(View.GONE);
        emptyResultsImageView.setVisibility(View.VISIBLE);
        emptyResultsTextView.setVisibility(View.VISIBLE);
    }

    private void searchUsers(String query) {
        userViewModel.searchPagedUsers(query).observe(this, pagingData -> {
            userAdapter.submitData(getLifecycle(), pagingData);
            handleEmptyResults();

        });
    }


    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(MainActivity.this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int itemPosition = viewHolder.getAdapterPosition();
                User user = userAdapter.getUser(itemPosition);
                if(direction == ItemTouchHelper.LEFT) {

                    Snackbar snackbar = Snackbar.make(viewHolder.itemView,"You removed "+user.getFirst_name() + " " + user.getLast_name(),Snackbar.LENGTH_LONG);
                    snackbar.setAction("UNDO", v -> {

                        userViewModel.insertUser(user).observe(MainActivity.this, success -> {
                            if ("success".equals(success)) {
                                Toast.makeText(MainActivity.this, user.getFirst_name() + " " + user.getLast_name() + " restored successfully!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        Toast.makeText(MainActivity.this, user.getFirst_name() + " " + user.getLast_name() + " restored successfully!", Toast.LENGTH_SHORT).show();

                    });

                    AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                            .setMessage("Do you want to delete \"" + user.getFirst_name() + " " + user.getLast_name() + "\"?")
                            .setView(R.layout.custom_dialog_buttons)  // Set the custom layout for the buttons
                            .setCancelable(false)
                            .create();

                    dialog.setOnShowListener(dialogInterface -> {

                        Button positiveButton = dialog.findViewById(R.id.dialog_positive_button);
                        Button negativeButton = dialog.findViewById(R.id.dialog_negative_button);

                        positiveButton.setOnClickListener(v -> {
                            if(itemPosition!=-1){

                                userViewModel.deleteUser(user).observe(MainActivity.this, success -> {

                                    if ("success".equals(success)) {
                                        Toast.makeText(MainActivity.this, user.getFirst_name() + " " + user.getLast_name() + " removed successfully!", Toast.LENGTH_SHORT).show();
                                        snackbar.show();
                                        dialog.dismiss();
                                    }
                                });
                            }

                        });

                        negativeButton.setOnClickListener(v -> {
                            userAdapter.notifyItemChanged(itemPosition);
                            dialog.dismiss();
                        });
                    });

                    dialog.show();
                }
            }
        };
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    private void navigateToAddUserActivity(){
        floatingActionButton.setOnClickListener(view -> {
                    startActivity(new Intent(MainActivity.this, AddUserActivity.class));
                }
        );
    }

    @Override
    public void onClickUser(User user) {
            Intent intent = new Intent(MainActivity.this,UserDetailsActivity.class);
            intent.putExtra("model",user);
            startActivity(intent);
    }
}