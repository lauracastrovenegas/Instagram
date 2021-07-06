package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.instagram.ui.login.LoginActivity;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

import models.Post;

public class MainActivity extends AppCompatActivity {
    
    public static final String TAG = "MainActivity";
    private EditText etDescription;
    private Button btnCapture;
    private ImageView ivPostImage;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        etDescription = findViewById(R.id.etDescription);
        btnCapture = findViewById(R.id.btnCapture);
        ivPostImage = findViewById(R.id.ivPostImage);
        btnSubmit = findViewById(R.id.btnSubmit);
        
        //queryPosts();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = etDescription.getText().toString();
                if (description.isEmpty()){
                    Toast.makeText(MainActivity.this, "Description cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(description, currentUser);
            }
        });
    }

    private void savePost(String description, ParseUser currentUser) {
        Post post = new Post();
        post.setDescription(description);
        post.setUser(currentUser);
        //post.setImage();
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(MainActivity.this, "Error while saving!", Toast.LENGTH_SHORT).show();
                }

                Log.i(TAG, "Post save was successful!");
                etDescription.setText("");
            }
        });
    }

    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null){
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }

                for (Post post : posts){
                    Log.i(TAG, "Post: " + post.getDescription() + ", username:" + post.getUser().getUsername());
                }
            }
        });
    }

    public void onLogout(View view){
        ParseUser.logOut();
        finish();
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
    }
}