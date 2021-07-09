package com.example.instagram.ui.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.example.instagram.adapters.PostAdapter;
import com.example.instagram.adapters.PostPreviewAdapter;
import com.example.instagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileActivity";
    public static final int POST_LIMIT = 20;
    public static final String DESCENDING_ORDER_KEY = "createdAt";
    public static final String QUERY_ERROR = "Error getting posts";

    RecyclerView rvTimeline;
    PostPreviewAdapter adapter;
    List<Post> posts;

    ImageView ivProfilePicture;
    TextView tvName;
    TextView tvUsername;

    public ProfileFragment(){}

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        posts = new ArrayList<>();

        adapter = new PostPreviewAdapter(getActivity(), posts);

        rvTimeline = view.findViewById(R.id.rvProfileGrid);
        rvTimeline.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rvTimeline.setAdapter(adapter);

        ivProfilePicture = view.findViewById(R.id.ivProfileImage);
        tvName = view.findViewById(R.id.tvProfileName);
        tvUsername = view.findViewById(R.id.tvProfileUsername);

        tvName.setText(ParseUser.getCurrentUser().getString("name"));
        tvUsername.setText(ParseUser.getCurrentUser().getString("username"));

        ParseFile profileImage = ParseUser.getCurrentUser().getParseFile("image");
        if (profileImage != null) {
            Glide.with(getContext())
                    .load(profileImage.getUrl())
                    .override(100, 100)
                    .circleCrop()
                    .into(ivProfilePicture);
        }

        queryPosts();

        return view;
    }

    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.setLimit(POST_LIMIT);
        query.addDescendingOrder(DESCENDING_ORDER_KEY);
        query.findInBackground(new FindCallback<Post>() {

            @Override
            public void done(List<Post> allPosts, ParseException e) {
                if (e != null){
                    Log.e(TAG, QUERY_ERROR, e);
                    return;
                }

                posts.addAll(allPosts);
                adapter.notifyDataSetChanged();
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