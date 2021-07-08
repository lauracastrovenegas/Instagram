package com.example.instagram;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.io.File;

public class SignupActivity extends AppCompatActivity {

    public static final String TAG = "SignupActivity";
    public static final String LOGIN_ISSUE_STRING = "Issue with login!";
    public static final String SIGN_UP_ISSUE = "Account already exists for this username/email!";
    public static final String SUCCESS_MSG = "Success!";
    public static final String FAILURE_MSG = "Failure: ";
    public static final String WELCOME_STRING = "Welcome to Instagram!";
    public static final String KEY_NAME = "name";
    public static final String KEY_PROFILE_IMAGE = "profilePicture";
    public static final String CAMERA_FAILURE = "Picture wasn't taken!";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 52;

    private File photoFile;
    public String photoFileName = "profilePhoto.jpg";

    TextView tvLogin;
    Button btnSignup;
    Button btnTakePhoto;
    //Button btnSelectPhoto;
    EditText etName;
    EditText etEmail;
    EditText etUsername;
    EditText etPassword;
    ImageView ivProfilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        tvLogin = findViewById(R.id.tvGoToLogin);
        btnSignup = findViewById(R.id.btnSubmitProfile);
        btnTakePhoto = findViewById(R.id.btnUploadProfilePic);
        //btnSelectPhoto = findViewById(R.id.btnUploadProfilePic2);
        etName = findViewById(R.id.etNameInput);
        etEmail = findViewById(R.id.etEmailInput);
        etUsername = findViewById(R.id.etUsernameInput);
        etPassword = findViewById(R.id.etPasswordInput);
        ivProfilePicture = findViewById(R.id.ivProfilePictureUploaded);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupUser(etName.getText().toString(), etEmail.getText().toString(), etUsername.getText().toString() ,etPassword.getText().toString(), photoFile);
            }
        });

        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });
    }

    private void signupUser(String name, String email, String username, String password, File image){

        ParseUser user = new ParseUser();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        user.put(KEY_NAME, name);
        //user.setImage(image);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    loginUser(username, password);
                    Toast.makeText(SignupActivity.this, WELCOME_STRING, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(SignupActivity.this, SIGN_UP_ISSUE, Toast.LENGTH_LONG).show();
                    Log.e(TAG, SIGN_UP_ISSUE, e);
                }
            }
        });
    }

    public void loginUser(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null){
                    // TODO: Better error handling
                    Toast.makeText(SignupActivity.this, LOGIN_ISSUE_STRING, Toast.LENGTH_LONG).show();
                    Log.e(TAG, LOGIN_ISSUE_STRING + String.valueOf(e.getCode()), e);
                    return;
                }

                goMainActivity();
            }
        });
    }

    private void launchCamera() {
        // create intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //intent.setPackage("name of app here (look in Manifest)"); specify which app takes care of intent

        // Create a File reference to access to future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap file object into a content provider
        Uri fileProvider = FileProvider.getUriForFile(SignupActivity.this, "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                ivProfilePicture.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(this, CAMERA_FAILURE, Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    private File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, FAILURE_MSG);
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    public void goLoginActivity (View view){
        Intent i = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    private void goMainActivity() {
        Intent i = new Intent(this, TimelineActivity.class);
        startActivity(i);
        finish();
    }
}