package com.example.instagram.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.instagram.LoginActivity;
import com.example.instagram.MainActivity;
import com.example.instagram.R;
import com.example.instagram.TimelineActivity;
import com.example.instagram.adapters.PostAdapter;
import com.example.instagram.databinding.FragmentHomeBinding;
import com.example.instagram.models.Post;
import com.example.instagram.ui.compose.ComposeFragment;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    public static final String TAG = "TimelineActivity";
    public static final int POST_LIMIT = 20;
    public static final String DESCENDING_ORDER_KEY = "createdAt";
    public static final String QUERY_ERROR = "Error getting posts";

    private SwipeRefreshLayout swipeContainer;
    RecyclerView rvTimeline;
    PostAdapter adapter;
    List<Post> posts;

    public HomeFragment(){}

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        posts = new ArrayList<>();

        adapter = new PostAdapter(getActivity(), posts);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        rvTimeline = view.findViewById(R.id.rvTimeline);
        rvTimeline.setLayoutManager(linearLayoutManager);
        rvTimeline.setAdapter(adapter);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                queryPosts();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        queryPosts();

        return view;
    }

    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(POST_LIMIT);
        query.addDescendingOrder(DESCENDING_ORDER_KEY);
        query.findInBackground(new FindCallback<Post>() {

            @Override
            public void done(List<Post> allPosts, ParseException e) {
                if (e != null){
                    Log.e(TAG, QUERY_ERROR, e);
                    return;
                }

                // CLEAR OUT old items before appending in the new ones for refresh
                posts.clear();
                adapter.clear();
                posts.addAll(allPosts);
                adapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }
        });
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment_activity_main, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}