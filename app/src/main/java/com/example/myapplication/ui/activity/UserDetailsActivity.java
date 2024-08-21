package com.example.myapplication.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.models.User;
import com.example.myapplication.utils.ValidationUtils;
import com.example.myapplication.viewmodel.UserViewModel;

import java.util.concurrent.atomic.AtomicBoolean;

public class UserDetailsActivity extends AppCompatActivity {

    private ImageView userAvatarImageView,uploadIcon;
    private TextView userNameTextView;
    private Button updateButton, cancelButton;
    private EditText userEmailEditText, firstNameEditText, lastNameEditText;
    UserViewModel usersViewModel;
    private Uri selectedImageUri;
    private TextView errorFirstNameTextView, errorLastNameTextView,errorMissingEmailTextView, errorInvalidEmailTextView,generalFeedbackTextView;

    private User userToUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_user_details), (v, insets) -> {
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
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);

        }

            initViews();
            setupViewModel();
            setGravityEditTexts();
            restoreImageView(savedInstanceState);
            restoreErrorTexts(savedInstanceState);
            restoreEditTextsTexts(savedInstanceState);
            initUploadIconClickListener();
            initUpdateButtonClickListener();
            navigateToMainActivity();


            if(getIntent().hasExtra("model")){
                userToUpdate = getIntent().getParcelableExtra("model");

                if(userToUpdate!=null){
                    loadUser(userToUpdate);
                }
            }

    }

    private void loadUser(User user) {

        userNameTextView.setText(String.format("%s %s", userToUpdate.getFirst_name(), userToUpdate.getLast_name()));
        firstNameEditText.setText(userToUpdate.getFirst_name());
        lastNameEditText.setText(userToUpdate.getLast_name());
        userEmailEditText.setText(userToUpdate.getEmail());
        initUserAvatarClickListener(userToUpdate.getAvatar());

        if(selectedImageUri!=null) {
            Glide.with(this)
                    .load(Uri.parse(selectedImageUri.toString()))
                    .circleCrop()
                    .into(userAvatarImageView);
            initUserAvatarClickListener(selectedImageUri);

        }else if(userToUpdate.getAvatar() != null){
            Glide.with(this)
                    .load(Uri.parse(user.getAvatar()))
                    .circleCrop()
                    .into(userAvatarImageView);
            initUserAvatarClickListener(userToUpdate.getAvatar());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    private void initViews(){
        uploadIcon = findViewById(R.id.upload_icon_user_details);
        userAvatarImageView = findViewById(R.id.user_avatar);
        userNameTextView = findViewById(R.id.user_name_header);
        userEmailEditText = findViewById(R.id.edit_text_email);
        firstNameEditText = findViewById(R.id.edit_text_first_name);
        lastNameEditText = findViewById(R.id.edit_text_last_name);
        updateButton = findViewById(R.id.button_update);
        cancelButton = findViewById(R.id.button_cancel);
        errorFirstNameTextView = findViewById(R.id.error_label_first_name_update_user);
        errorLastNameTextView = findViewById(R.id.error_label_last_name_update_user);
        errorMissingEmailTextView =findViewById(R.id.error_label_email_missing_update_user);
        errorInvalidEmailTextView = findViewById(R.id.error_label_email_update_user);
        generalFeedbackTextView = findViewById(R.id.general_feedback_text_update_user);
    }

    private void setGravityEditTexts(){
        userEmailEditText.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        userEmailEditText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);

        firstNameEditText.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        firstNameEditText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);

        lastNameEditText.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        lastNameEditText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
    }

    private void restoreErrorTexts(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Restore visibility states of error TextViews
            if (errorMissingEmailTextView != null) {
                errorMissingEmailTextView.setVisibility(savedInstanceState.getInt("errorMissingEmailVisibility"));
            }
            if (errorInvalidEmailTextView != null) {
                errorInvalidEmailTextView.setVisibility(savedInstanceState.getInt("errorInvalidEmailVisibility"));
            }
            if (errorFirstNameTextView != null) {
                errorFirstNameTextView.setVisibility(savedInstanceState.getInt("errorFirstNameVisibility"));
            }
            if (errorLastNameTextView != null) {
                errorLastNameTextView.setVisibility(savedInstanceState.getInt("errorLastNameVisibility"));
            }

            // Restore the state of generalFeedbackTextView
            if (generalFeedbackTextView != null) {
                generalFeedbackTextView.setVisibility(savedInstanceState.getInt("generalFeedbackVisibility"));
                generalFeedbackTextView.setText(savedInstanceState.getString("generalFeedbackText"));
            }
        }
    }

    private void restoreImageView(@Nullable Bundle savedInstanceState){
        if (savedInstanceState != null) {
            String uriString = savedInstanceState.getString("selectedImageUri");
            if (uriString != null) {
                selectedImageUri = Uri.parse(uriString);
                Glide.with(this)
                        .load(selectedImageUri)
                        .into(userAvatarImageView);
            }
        }
    }

    private void restoreEditTextsTexts(@Nullable Bundle savedInstanceState){
        if (savedInstanceState != null) {
            userEmailEditText.setText(savedInstanceState.getString("email", ""));
            firstNameEditText.setText(savedInstanceState.getString("firstName", ""));
            lastNameEditText.setText(savedInstanceState.getString("lastName", ""));
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the visibility states of error TextViews
        outState.putInt("errorMissingEmailVisibility", errorMissingEmailTextView.getVisibility());
        outState.putInt("errorInvalidEmailVisibility", errorInvalidEmailTextView.getVisibility());
        outState.putInt("errorFirstNameVisibility", errorFirstNameTextView.getVisibility());
        outState.putInt("errorLastNameVisibility", errorLastNameTextView.getVisibility());

        // Save the state of generalFeedbackTextView
        outState.putInt("generalFeedbackVisibility", generalFeedbackTextView.getVisibility());
        outState.putString("generalFeedbackText", generalFeedbackTextView.getText().toString());

        outState.putString("email", userEmailEditText.getText().toString());
        outState.putString("firstName", firstNameEditText.getText().toString());
        outState.putString("lastName", lastNameEditText.getText().toString());

        if (selectedImageUri != null) {
            outState.putString("selectedImageUri", selectedImageUri.toString());
        }
    }


    private void setupViewModel() {
        usersViewModel = UserViewModel.getInstance(getApplication());
    }

    private LinearLayout createCustomActionBarView() {
        // Create a TextView for the title
        TextView textView = new TextView(this);
        textView.setText(R.string.update_user_title);
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

    private void createDialog(Uri imageUri){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_image);
        ImageView dialogImage = dialog.findViewById(R.id.dialogImage);

        Glide.with(this)
                .load(imageUri)
                .into(dialogImage);

        dialog.show();
    }

    private void createDialog(String imageUri){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_image);
        ImageView dialogImage = dialog.findViewById(R.id.dialogImage);

        Glide.with(this)
                .load(imageUri)
                .into(dialogImage);

        dialog.show();
    }

    private void initUserAvatarClickListener(String imageUri) {
        userAvatarImageView.setOnClickListener(v -> showImageDialog(imageUri));
    }

    private void initUserAvatarClickListener(Uri imageUri) {
        userAvatarImageView.setOnClickListener(v -> showImageDialogSelectedImageUri(imageUri));
    }

    private void showImageDialogSelectedImageUri(Uri imageUri) {
        if(imageUri!=null){
            createDialog(imageUri);
        }
    }

    private void showImageDialog(String imageUri) {
        if(imageUri!=null){
            createDialog(imageUri);
        }
    }

    private void initUploadIconClickListener() {
        uploadIcon.setOnClickListener(v -> {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });
    }

    private void showEmailExistsError(String email) {
        generalFeedbackTextView.setVisibility(View.VISIBLE);
        generalFeedbackTextView.setText(String.format("*User with this email: %s already exists", email));
        Toast.makeText(UserDetailsActivity.this, "Error: Email " + email + " already exists", Toast.LENGTH_SHORT).show();
    }

    private void showUnexpectedError(){
        generalFeedbackTextView.setVisibility(View.VISIBLE);
        generalFeedbackTextView.setText(R.string.unexpected_error_occured);
        Toast.makeText(UserDetailsActivity.this, "*An unexpected error occurred", Toast.LENGTH_SHORT).show();
    }


    private void initUpdateButtonClickListener(){
        updateButton.setOnClickListener(v -> {

            AtomicBoolean hasError = new AtomicBoolean(false);

            // Reset error messages visibility
            errorMissingEmailTextView.setVisibility(View.GONE);
            errorInvalidEmailTextView.setVisibility(View.GONE);
            errorFirstNameTextView.setVisibility(View.GONE);
            errorLastNameTextView.setVisibility(View.GONE);
            generalFeedbackTextView.setVisibility(View.GONE);

            if(userToUpdate!=null){
                String firstName = firstNameEditText.getText().toString().trim();
                String lastName = lastNameEditText.getText().toString().trim();
                String email = userEmailEditText.getText().toString().trim();

                // Validate email presence
                if (!ValidationUtils.validateEmail(email)) {
                    errorMissingEmailTextView.setVisibility(View.VISIBLE);
                    hasError.set(true);
                }

                // Validate email format
                if (ValidationUtils.validateEmail(email) && !ValidationUtils.isValidEmail(email)) {
                    errorInvalidEmailTextView.setVisibility(View.VISIBLE);
                    hasError.set(true);
                }

                // Validate first name
                if (!ValidationUtils.validateFirstName(firstName)) {
                    errorFirstNameTextView.setVisibility(View.VISIBLE);
                    hasError.set(true);
                } else {
                    errorFirstNameTextView.setVisibility(View.GONE);
                }

                // Validate last name
                if (!ValidationUtils.validateLastname(lastName)) {
                    errorLastNameTextView.setVisibility(View.VISIBLE);
                    hasError.set(true);
                } else {
                    errorLastNameTextView.setVisibility(View.GONE);
                }

                // If there are errors, return early
                if (hasError.get()) {
                    Toast.makeText(this, "Please correct the errors and try again.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // No errors, proceed with updating the user
                userToUpdate.setFirst_name(firstName);
                userToUpdate.setLast_name(lastName);
                userToUpdate.setEmail(email);

                if (selectedImageUri != null) {
                    userToUpdate.setAvatar(selectedImageUri.toString());
                }

                LiveData<String> result = usersViewModel.updateUser(userToUpdate);

                result.observe(this, new Observer<String>() {
                    @Override
                    public void onChanged(String result) {
                        if ("success".equals(result)) {
                            Toast.makeText(UserDetailsActivity.this, "User " + firstName + " " + lastName + " added successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else if ("constraint_failure".equals(result)) {
                            showEmailExistsError(email);
                        } else {
                            showUnexpectedError();
                        }
                    }
                });
            }
        });
    }


    private void navigateToMainActivity(){
        cancelButton.setOnClickListener(v -> {
            finish();
        });
    }

    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Glide.with(UserDetailsActivity.this)
                            .load(uri) // Load the URI directly, not the string
                            .into(userAvatarImageView);
                    initUserAvatarClickListener(selectedImageUri);

                } else {
                    Toast.makeText(UserDetailsActivity.this, "No media selected", Toast.LENGTH_SHORT).show();
                }
            });
}