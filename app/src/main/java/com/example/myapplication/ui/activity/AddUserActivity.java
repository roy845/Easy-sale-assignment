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
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.constants.Constants;
import com.example.myapplication.models.User;
import com.example.myapplication.utils.ValidationUtils;
import com.example.myapplication.viewmodel.UserViewModel;
import com.google.android.material.button.MaterialButton;
import java.util.Date;
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

        initViews();
        setActionBar();
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

    private void setActionBar(){
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
    }

    private void restoreImageDialog(Bundle savedInstanceState){
        if (savedInstanceState != null) {
            boolean wasDialogShowing = savedInstanceState.getBoolean(Constants.IS_DIALOG_SHOWING, false);
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
        uploadIcon.setOnClickListener(v -> pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build()));
    }

    private void showImageDialog() {
        if(selectedImageUri!=null){
            createDialog();
        }
    }

    private void initUserAvatarClickListener() {
        userAvatarImageView.setOnClickListener(v -> showImageDialog());
    }

    private void setGravityEditTexts() {

        userEmailEditText.setTextDirection(View.TEXT_DIRECTION_LTR);
        firstNameEditText.setTextDirection(View.TEXT_DIRECTION_LTR);
        lastNameEditText.setTextDirection(View.TEXT_DIRECTION_LTR);

        userEmailEditText.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        firstNameEditText.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        lastNameEditText.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
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
            if (errorImageViewTextView != null) {
                errorImageViewTextView.setVisibility(savedInstanceState.getInt(Constants.ERROR_IMAGE_VIEW_VISIBILITY));
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
        outState.putInt(Constants.ERROR_IMAGE_VIEW_VISIBILITY, errorImageViewTextView.getVisibility());


        outState.putInt(Constants.ERROR_GENERAL_FEEDBACK_VISIBILITY, generalFeedbackTextView.getVisibility());
        outState.putString(Constants.ERROR_GENERAL_FEEDBACK_TEXT, generalFeedbackTextView.getText().toString());

        outState.putString(Constants.EMAIL, userEmailEditText.getText().toString());
        outState.putString(Constants.FIRST_NAME, firstNameEditText.getText().toString());
        outState.putString(Constants.LAST_NAME, lastNameEditText.getText().toString());

        if (selectedImageUri != null) {
            outState.putString(Constants.SELECTED_IMAGE_URI, selectedImageUri.toString());
        }

        outState.putBoolean(Constants.IS_DIALOG_SHOWING, imageDialog != null && imageDialog.isShowing());

    }

    private void showEmailExistsError(String email) {
        generalFeedbackTextView.setVisibility(View.VISIBLE);
        generalFeedbackTextView.setText(String.format(Constants.USER_ALREADY_EXISTS, email));
        Toast.makeText(AddUserActivity.this, "Error: Email " + email + " already exists", Toast.LENGTH_SHORT).show();
    }

    private void showUnexpectedError(){
        generalFeedbackTextView.setVisibility(View.VISIBLE);
        generalFeedbackTextView.setText(R.string.unexpected_error_occured);
        Toast.makeText(AddUserActivity.this,Constants.UNEXPECTED_ERROR_OCCURRED, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, Constants.CORRECT_ERRORS_AND_TRY_AGAIN, Toast.LENGTH_SHORT).show();
                return;
            }

            User newUser = new User(firstName, lastName, email, selectedImageUri !=null ? selectedImageUri.toString() : null,new Date(),new Date());

            LiveData<String> result = usersViewModel.insertUser(newUser);
            result.observe(this, result1 -> {
                if (Constants.SUCCESS.equals(result1)) {
                    Toast.makeText(AddUserActivity.this, "User " + firstName + " " + lastName + " added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (Constants.CONSTRAINT_FAILURE.equals(result1)) {
                    showEmailExistsError(email);
                } else {
                    showUnexpectedError();
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
        usersViewModel = new ViewModelProvider(this).get(UserViewModel.class);
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
                    Toast.makeText(AddUserActivity.this, Constants.NO_MEDIA_SELECTED, Toast.LENGTH_SHORT).show();
                }
            });
}