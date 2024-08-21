package com.example.myapplication.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.myapplication.R;
import com.example.myapplication.viewmodel.UserViewModel;
import com.google.android.material.button.MaterialButton;

public class ErrorLoadingUsers extends AppCompatActivity {
    private UserViewModel userViewModel;
    private MaterialButton retryButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_error_loading_users);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.errorLoadingUsers), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            // Set the default title to an empty string to clear it
            actionBar.setTitle("");

            // Create the custom action bar view using the extracted method
            LinearLayout customActionBarView = createCustomActionBarView();

            // Add the container to the ActionBar
            ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    Gravity.END | Gravity.CENTER_VERTICAL // Align to right and center vertically
            );

            actionBar.setCustomView(customActionBarView, params);
            actionBar.setDisplayShowCustomEnabled(true);
        }

        initializeViews();
        setupViewModel();
        initRetryButtonClickListener();
    }

    private LinearLayout createCustomActionBarView() {
        // Create a TextView for the title
        TextView textView = new TextView(this);
        textView.setText(R.string.error_loading_users_title);
        textView.setTextColor(Color.WHITE); // Set text color
        textView.setTextSize(18); // Set text size
        textView.setGravity(Gravity.END); // Align text to the right


        ImageView imageView = getImageView();

        // Create a container to hold both the TextView and ImageView
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        linearLayout.addView(imageView); // Add the icon first
        linearLayout.addView(textView); // Add the title next to the icon

        return linearLayout;
    }

    private @NonNull ImageView getImageView() {
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.baseline_people_24); // Replace with your icon resource

        // Set layout parameters with left margin
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        imageParams.setMargins(16, 0, 0, 0); // Set left margin (16 pixels)

        imageView.setLayoutParams(imageParams); // Apply the parameters to the ImageView
        return imageView;
    }

    private void initializeViews(){
        retryButton = findViewById(R.id.retry_button);
    }

    public void initRetryButtonClickListener(){
        retryButton.setOnClickListener(v-> {

            userViewModel.retryLoadUsers();
            userViewModel.getErrorLiveData().observe(ErrorLoadingUsers.this, error -> {
                if(!error){
                    finish();
                }
            });
        });
    }

    /**
     * Setup the ViewModel for the fragment.
     */
    private void setupViewModel() {
        userViewModel = UserViewModel.getInstance(getApplication());
    }

}