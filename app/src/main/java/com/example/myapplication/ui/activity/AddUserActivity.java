package com.example.myapplication.ui.activity;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
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
import com.google.android.material.button.MaterialButton;
import java.util.concurrent.atomic.AtomicBoolean;

public class AddUserActivity extends AppCompatActivity {
    private ImageView userAvatarImageView,uploadIcon;
    TextView addUserTitleTextView;
    MaterialButton addUserButton;
    MaterialButton cancelButton;
    private EditText userEmailEditText, firstNameEditText, lastNameEditText;
    UserViewModel usersViewModel;
    private Uri selectedImageUri;
    private TextView errorMissingEmailTextView, errorInvalidEmailTextView, errorFirstNameTextView, errorLastNameTextView,
            errorImageViewTextView,generalFeedbackTextView;
    private Dialog imageDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_add_user), (v, insets) -> {
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
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        initViews();
        setupViewModel();
        setGravityEditTexts();
        initUserAvatarClickListener();
        initAddButtonClickListener();
        initUploadIconClickListener();
        restoreErrorTexts(savedInstanceState);
        restoreImageView(savedInstanceState);
        restoreEditTextsTexts(savedInstanceState);
        restoreImageDialog(savedInstanceState);
        navigateToMainActivity();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void restoreImageDialog(Bundle savedInstanceState){
        if (savedInstanceState != null) {
            boolean wasDialogShowing = savedInstanceState.getBoolean("isDialogShowing", false);
            if (wasDialogShowing) {
                createDialog();
            }
        }
    }

    private void createDialog(){
        if (imageDialog == null || !imageDialog.isShowing()) {
            imageDialog = new Dialog(this);
            imageDialog.setContentView(R.layout.dialog_image);
            ImageView dialogImage = imageDialog.findViewById(R.id.dialogImage);

            if (selectedImageUri != null) {
                Glide.with(this)
                        .load(selectedImageUri)
                        .into(dialogImage);
            }

            imageDialog.show();
        }
    }

    private void initUploadIconClickListener() {
        uploadIcon.setOnClickListener(v -> {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });
    }

    private void showImageDialog() {
        if(selectedImageUri!=null){
            createDialog();
        }
    }

    private void initUserAvatarClickListener() {
        userAvatarImageView.setOnClickListener(v -> showImageDialog());
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
            if (errorImageViewTextView != null) {
                errorImageViewTextView.setVisibility(savedInstanceState.getInt("errorImageViewVisibility"));
            }


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


        outState.putInt("errorMissingEmailVisibility", errorMissingEmailTextView.getVisibility());
        outState.putInt("errorInvalidEmailVisibility", errorInvalidEmailTextView.getVisibility());
        outState.putInt("errorFirstNameVisibility", errorFirstNameTextView.getVisibility());
        outState.putInt("errorLastNameVisibility", errorLastNameTextView.getVisibility());
        outState.putInt("errorImageViewVisibility", errorImageViewTextView.getVisibility());


        outState.putInt("generalFeedbackVisibility", generalFeedbackTextView.getVisibility());
        outState.putString("generalFeedbackText", generalFeedbackTextView.getText().toString());

        outState.putString("email", userEmailEditText.getText().toString());
        outState.putString("firstName", firstNameEditText.getText().toString());
        outState.putString("lastName", lastNameEditText.getText().toString());

        if (selectedImageUri != null) {
            outState.putString("selectedImageUri", selectedImageUri.toString());
        }

        outState.putBoolean("isDialogShowing", imageDialog != null && imageDialog.isShowing());

    }

    private void showEmailExistsError(String email) {
        generalFeedbackTextView.setVisibility(View.VISIBLE);
        generalFeedbackTextView.setText(String.format("*User with this email: %s already exists", email));
        Toast.makeText(AddUserActivity.this, "Error: Email " + email + " already exists", Toast.LENGTH_SHORT).show();
    }

    private void showUnexpectedError(){
        generalFeedbackTextView.setVisibility(View.VISIBLE);
        generalFeedbackTextView.setText(R.string.unexpected_error_occured);
        Toast.makeText(AddUserActivity.this, "*An unexpected error occurred", Toast.LENGTH_SHORT).show();
    }

    private void initAddButtonClickListener() {

        addUserButton.setOnClickListener(v -> {

           String firstName = firstNameEditText.getText().toString().trim();
           String lastName = lastNameEditText.getText().toString().trim();
           String email = userEmailEditText.getText().toString().trim();

            AtomicBoolean hasError = new AtomicBoolean(false);


            errorMissingEmailTextView.setVisibility(View.GONE);
            errorInvalidEmailTextView.setVisibility(View.GONE);
            errorFirstNameTextView.setVisibility(View.GONE);
            errorLastNameTextView.setVisibility(View.GONE);
            errorImageViewTextView.setVisibility(View.GONE);
            generalFeedbackTextView.setVisibility(View.GONE);


            if (!ValidationUtils.validateEmail(email)) {
                errorMissingEmailTextView.setVisibility(View.VISIBLE);
                hasError.set(true);
            }


            if (ValidationUtils.validateEmail(email) && ValidationUtils.isValidEmail(email)) {
                errorInvalidEmailTextView.setVisibility(View.VISIBLE);
                hasError.set(true);
            }

            if (ValidationUtils.validateFirstName(firstName)) {
                errorFirstNameTextView.setVisibility(View.VISIBLE);
                hasError.set(true);
            }

            if (ValidationUtils.validateLastname(lastName)) {
                errorLastNameTextView.setVisibility(View.VISIBLE);
                hasError.set(true);
            }

            if (!ValidationUtils.validateImageView(userAvatarImageView) && selectedImageUri == null) {
                errorImageViewTextView.setVisibility(View.VISIBLE);
                hasError.set(true);
            }

            if (hasError.get()) {
                Toast.makeText(this, "Please correct the errors and try again.", Toast.LENGTH_SHORT).show();
                return;
            }

            User newUser = new User(firstName, lastName, email, selectedImageUri !=null ? selectedImageUri.toString() : null);

            LiveData<String> result = usersViewModel.insertUser(newUser);
            result.observe(this, new Observer<String>() {
                @Override
                public void onChanged(String result) {
                    if ("success".equals(result)) {
                        Toast.makeText(AddUserActivity.this, "User " + firstName + " " + lastName + " added successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else if ("constraint_failure".equals(result)) {
                        showEmailExistsError(email);
                    } else {
                        showUnexpectedError();
                    }
                }
            });
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private LinearLayout createCustomActionBarView() {

        TextView textView = new TextView(this);
        textView.setText(R.string.add_new_user_title);
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

    private void initViews(){
        addUserTitleTextView = findViewById(R.id.add_user_title);
        addUserButton = findViewById(R.id.add_user_button);
        cancelButton = findViewById(R.id.cancel_add_user_button);
        userAvatarImageView = findViewById(R.id.add_user_avatar);
        uploadIcon = findViewById(R.id.upload_avatar);
        userEmailEditText = findViewById(R.id.add_edit_text_email);
        firstNameEditText= findViewById(R.id.add_edit_text_first_name);
        lastNameEditText= findViewById(R.id.add_edit_text_last_name);
        errorMissingEmailTextView = findViewById(R.id.error_label_email_missing);
        errorInvalidEmailTextView = findViewById(R.id.error_label_email);
        errorFirstNameTextView = findViewById(R.id.error_label_first_name);
        errorLastNameTextView = findViewById(R.id.error_label_last_name);
        errorImageViewTextView =  findViewById(R.id.error_label_avatar_missing);
        generalFeedbackTextView = findViewById(R.id.general_feedback_text);
    }

    private void setupViewModel() {
        usersViewModel = UserViewModel.getInstance(getApplication());
    }

    private void navigateToMainActivity(){
        cancelButton.setOnClickListener(v -> finish());
    }

    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Glide.with(AddUserActivity.this)
                            .load(uri)
                            .into(userAvatarImageView);
                } else {
                    Toast.makeText(AddUserActivity.this, "No media selected", Toast.LENGTH_SHORT).show();
                }
            });
}