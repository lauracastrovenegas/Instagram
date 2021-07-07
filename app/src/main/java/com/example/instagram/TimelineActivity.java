package com.example.instagram;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.adapters.PostAdapter;
import com.example.instagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class TimelineActivity extends AppCompatActivity {

    public static final String TAG = "com.example.instagram.TimelineActivity";

    RecyclerView rvTimeline;
    PostAdapter adapter;
    List<Post> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        posts = new ArrayList<>();

        adapter = new PostAdapter(this, posts);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        rvTimeline = findViewById(R.id.rvTimeline);
        rvTimeline.setLayoutManager(linearLayoutManager);
        rvTimeline.setAdapter(adapter);

        queryPosts();
    }

    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(20);
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> allPosts, ParseException e) {
                if (e != null){
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }

                posts.addAll(allPosts);
                adapter.notifyDataSetChanged();
            }
        });
    }
}