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
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.constants.Constants;
import com.example.myapplication.models.User;
import com.example.myapplication.utils.ValidationUtils;
import com.example.myapplication.viewmodel.UserViewModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

public class UserDetailsActivity extends AppCompatActivity {

    private ImageView userAvatarImageView,uploadIcon;
    private TextView userNameTextView;
    private Button updateButton, cancelButton;
    private EditText userEmailEditText, firstNameEditText, lastNameEditText,createdAtEditText,updatedAtEditText;
    UserViewModel usersViewModel;
    private Uri selectedImageUri;
    private TextView errorFirstNameTextView, errorLastNameTextView,errorMissingEmailTextView, errorInvalidEmailTextView,generalFeedbackTextView;
    private Dialog imageDialog;

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

            initViews();
            setActionBar();
            setupViewModel();
            setGravityEditTexts();
            restoreImageView(savedInstanceState);
            restoreErrorTexts(savedInstanceState);
            restoreEditTextsTexts(savedInstanceState);
            restoreImageDialog(savedInstanceState);
            initUploadIconClickListener();
            initUpdateButtonClickListener();
            navigateToMainActivity();

            loadUser();
    }

    private void loadUser(){
        if(getIntent().hasExtra(Constants.USER_MODEL)){
            userToUpdate = getIntent().getParcelableExtra(Constants.USER_MODEL);

            if(userToUpdate!=null){
                loadUser(userToUpdate);
            }
        }
    }

    private void setActionBar(){
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {

            actionBar.setTitle(Constants.EMPTY_STRING);

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

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        if (userToUpdate.getCreatedAt() != null) {
            createdAtEditText.setText(dateFormat.format(userToUpdate.getCreatedAt()));
        }

        if (userToUpdate.getUpdatedAt() != null) {
            updatedAtEditText.setText(dateFormat.format(userToUpdate.getUpdatedAt()));
        }
    }

    private void restoreImageDialog(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            boolean wasDialogShowing = savedInstanceState.getBoolean(Constants.IS_DIALOG_SHOWING, false);
            String imageSourceType = savedInstanceState.getString(Constants.IMAGE_SOURCE_TYPE);

            if (wasDialogShowing && imageSourceType != null) {
                if (Constants.URI_STRING.equals(imageSourceType)) {
                    String uriString = savedInstanceState.getString(Constants.SELECTED_IMAGE_URI);
                    if (uriString != null) {
                        Uri imageUri = Uri.parse(uriString);
                        createDialog(imageUri);
                    }
                } else if (Constants.STRING.equals(imageSourceType)) {
                    String imageString = savedInstanceState.getString(Constants.SELECTED_IMAGE_STRING);
                    if (imageString != null) {
                        createDialog(imageString);
                    }
                }
            }
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
        createdAtEditText = findViewById(R.id.edit_text_created_at);
        updatedAtEditText = findViewById(R.id.edit_text_updated_at);
    }

    private void setGravityEditTexts() {

        userEmailEditText.setTextDirection(View.TEXT_DIRECTION_LTR);
        firstNameEditText.setTextDirection(View.TEXT_DIRECTION_LTR);
        lastNameEditText.setTextDirection(View.TEXT_DIRECTION_LTR);

        userEmailEditText.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        firstNameEditText.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        lastNameEditText.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);

        createdAtEditText.setTextDirection(View.TEXT_DIRECTION_LTR);
        createdAtEditText.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);

        updatedAtEditText.setTextDirection(View.TEXT_DIRECTION_LTR);
        updatedAtEditText.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
    }

    private void restoreErrorTexts(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {

            if (errorMissingEmailTextView != null) {
                errorMissingEmailTextView.setVisibility(savedInstanceState.getInt(Constants.ERROR_MISSING_EMAIL_VISIBILITY));
            }
            if (errorInvalidEmailTextView != null) {
                errorInvalidEmailTextView.setVisibility(savedInstanceState.getInt(Constants.ERROR_INVALID_EMAIL_VISIBILITY));
            }
            if (errorFirstNameTextView != null) {
                errorFirstNameTextView.setVisibility(savedInstanceState.getInt(Constants.ERROR_FIRST_NAME_VISIBILITY));
            }
            if (errorLastNameTextView != null) {
                errorLastNameTextView.setVisibility(savedInstanceState.getInt(Constants.ERROR_LAST_NAME_VISIBILITY));
            }

            if (generalFeedbackTextView != null) {
                generalFeedbackTextView.setVisibility(savedInstanceState.getInt(Constants.ERROR_GENERAL_FEEDBACK_VISIBILITY));
                generalFeedbackTextView.setText(savedInstanceState.getString(Constants.ERROR_GENERAL_FEEDBACK_TEXT));
            }
        }
    }

    private void restoreImageView(@Nullable Bundle savedInstanceState){
        if (savedInstanceState != null) {
            String uriString = savedInstanceState.getString(Constants.SELECTED_IMAGE_URI);
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
            userEmailEditText.setText(savedInstanceState.getString(Constants.EMAIL, Constants.EMPTY_STRING));
            firstNameEditText.setText(savedInstanceState.getString(Constants.FIRST_NAME, Constants.EMPTY_STRING));
            lastNameEditText.setText(savedInstanceState.getString(Constants.LAST_NAME, Constants.EMPTY_STRING));
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);


        outState.putInt(Constants.ERROR_MISSING_EMAIL_VISIBILITY, errorMissingEmailTextView.getVisibility());
        outState.putInt(Constants.ERROR_INVALID_EMAIL_VISIBILITY, errorInvalidEmailTextView.getVisibility());
        outState.putInt(Constants.ERROR_FIRST_NAME_VISIBILITY, errorFirstNameTextView.getVisibility());
        outState.putInt(Constants.ERROR_LAST_NAME_VISIBILITY, errorLastNameTextView.getVisibility());


        outState.putInt(Constants.ERROR_GENERAL_FEEDBACK_VISIBILITY, generalFeedbackTextView.getVisibility());
        outState.putString(Constants.ERROR_GENERAL_FEEDBACK_TEXT, generalFeedbackTextView.getText().toString());

        outState.putString(Constants.EMAIL, userEmailEditText.getText().toString());
        outState.putString(Constants.FIRST_NAME, firstNameEditText.getText().toString());
        outState.putString(Constants.LAST_NAME, lastNameEditText.getText().toString());

        if (selectedImageUri != null) {
            outState.putString(Constants.SELECTED_IMAGE_URI, selectedImageUri.toString());
            outState.putString(Constants.IMAGE_SOURCE_TYPE, Constants.URI_STRING);
        } else if (userToUpdate != null && userToUpdate.getAvatar() != null) {
            outState.putString(Constants.SELECTED_IMAGE_STRING, userToUpdate.getAvatar());
            outState.putString(Constants.IMAGE_SOURCE_TYPE, Constants.STRING);
        }

        outState.putBoolean(Constants.IS_DIALOG_SHOWING, imageDialog != null && imageDialog.isShowing());
    }


    private void setupViewModel() {
        usersViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    private LinearLayout createCustomActionBarView() {

        TextView textView = new TextView(this);
        textView.setText(R.string.update_user_title);
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

    private void createDialog(Uri imageUri) {
        if (imageDialog == null || !imageDialog.isShowing()) {
            imageDialog = new Dialog(this);
            imageDialog.setContentView(R.layout.dialog_image);
            ImageView dialogImage = imageDialog.findViewById(R.id.dialogImage);

            Glide.with(this)
                    .load(imageUri)
                    .into(dialogImage);

            imageDialog.show();
        }
    }

    private void createDialog(String imageString) {
        if (imageDialog == null || !imageDialog.isShowing()) {
            imageDialog = new Dialog(this);
            imageDialog.setContentView(R.layout.dialog_image);
            ImageView dialogImage = imageDialog.findViewById(R.id.dialogImage);

            Glide.with(this)
                    .load(imageString)
                    .into(dialogImage);

            imageDialog.show();
        }
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
        uploadIcon.setOnClickListener(v -> pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build()));
    }

    private void showEmailExistsError(String email) {
        generalFeedbackTextView.setVisibility(View.VISIBLE);
        generalFeedbackTextView.setText(String.format(Constants.USER_ALREADY_EXISTS, email));
        Toast.makeText(UserDetailsActivity.this, "Error: Email " + email + " already exists", Toast.LENGTH_SHORT).show();
    }

    private void showUnexpectedError(){
        generalFeedbackTextView.setVisibility(View.VISIBLE);
        generalFeedbackTextView.setText(R.string.unexpected_error_occured);
        Toast.makeText(UserDetailsActivity.this, Constants.UNEXPECTED_ERROR_OCCURRED, Toast.LENGTH_SHORT).show();
    }


    private void initUpdateButtonClickListener(){
        updateButton.setOnClickListener(v -> {

            AtomicBoolean hasError = new AtomicBoolean(false);

            errorMissingEmailTextView.setVisibility(View.GONE);
            errorInvalidEmailTextView.setVisibility(View.GONE);
            errorFirstNameTextView.setVisibility(View.GONE);
            errorLastNameTextView.setVisibility(View.GONE);
            generalFeedbackTextView.setVisibility(View.GONE);

            if(userToUpdate!=null){
                String firstName = firstNameEditText.getText().toString().trim();
                String lastName = lastNameEditText.getText().toString().trim();
                String email = userEmailEditText.getText().toString().trim();

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
                } else {
                    errorFirstNameTextView.setVisibility(View.GONE);
                }

                if (ValidationUtils.validateLastname(lastName)) {
                    errorLastNameTextView.setVisibility(View.VISIBLE);
                    hasError.set(true);
                } else {
                    errorLastNameTextView.setVisibility(View.GONE);
                }

                if (hasError.get()) {
                    Toast.makeText(this, Constants.CORRECT_ERRORS_AND_TRY_AGAIN, Toast.LENGTH_SHORT).show();
                    return;
                }

                userToUpdate.setFirst_name(firstName);
                userToUpdate.setLast_name(lastName);
                userToUpdate.setEmail(email);

                if (selectedImageUri != null) {
                    userToUpdate.setAvatar(selectedImageUri.toString());
                }

                userToUpdate.setUpdatedAt(new Date());

                LiveData<String> result = usersViewModel.updateUser(userToUpdate);

                result.observe(this, result1 -> {
                    if (Constants.SUCCESS.equals(result1)) {
                        Toast.makeText(UserDetailsActivity.this, "User " + firstName + " " + lastName + " updated successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (Constants.CONSTRAINT_FAILURE.equals(result1)) {
                        showEmailExistsError(email);
                    } else {
                        showUnexpectedError();
                    }
                });
            }
        });
    }


    private void navigateToMainActivity(){
        cancelButton.setOnClickListener(v -> finish());
    }

    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Glide.with(UserDetailsActivity.this)
                            .load(uri)
                            .into(userAvatarImageView);
                    initUserAvatarClickListener(selectedImageUri);

                } else {
                    Toast.makeText(UserDetailsActivity.this, Constants.NO_MEDIA_SELECTED, Toast.LENGTH_SHORT).show();
                }
            });
}