package com.example.instagram.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.instagram.MainActivity;
import com.example.instagram.R;
import com.example.instagram.TimelineActivity;
import com.example.instagram.ui.login.LoginViewModel;
import com.example.instagram.ui.login.LoginViewModelFactory;
import com.example.instagram.databinding.ActivityLoginBinding;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";
    public static final String LOGIN_ISSUE_STRING = "Issue with login!";
    public static final String SIGN_UP_ISSUE = "Issue with sign up!";
    public static final String SUCCESS_MSG = "Success!";
    public static final String WELCOME_STRING = "Welcome!";

    private ActivityLoginBinding binding;
    private ProgressBar loadingProgressBar;
    Button loginButton;
    EditText usernameEditText;
    EditText passwordEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (ParseUser.getCurrentUser() != null){
            goMainActivity();
        }

        usernameEditText = binding.username;
        passwordEditText = binding.password;
        loginButton = binding.login;
        loadingProgressBar = binding.loading;

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                //loginViewModel.login(usernameEditText.getText().toString(), passwordEditText.getText().toString());
                loginUser(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });
    }

    private void loginUser(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null){
                    // TODO: Better error handling
                    if (e.getCode() == ParseException.OBJECT_NOT_FOUND) {
                        signupUser(username, password);
                    } else {
                        Toast.makeText(LoginActivity.this, LOGIN_ISSUE_STRING, Toast.LENGTH_LONG).show();
                        Log.e(TAG, LOGIN_ISSUE_STRING + String.valueOf(e.getCode()), e);
                        loadingProgressBar.setVisibility(View.VISIBLE);
                    }
                    return;
                }

                goMainActivity();
                Toast.makeText(LoginActivity.this, SUCCESS_MSG, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signupUser(String username, String password){

        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    loginUser(username, password);
                    Toast.makeText(LoginActivity.this, WELCOME_STRING, Toast.LENGTH_SHORT).show();
                    goMainActivity();
                } else {
                    Toast.makeText(LoginActivity.this, SIGN_UP_ISSUE, Toast.LENGTH_LONG).show();
                    Log.e(TAG, SIGN_UP_ISSUE, e);
                }
            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, TimelineActivity.class);
        startActivity(i);
        finish();
    }

}