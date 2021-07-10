package com.example.instagram.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.instagram.DeviceDimensionsHelper;
import com.example.instagram.R;
import com.example.instagram.models.Post;
import com.example.instagram.ui.home.HomeFragment;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.Date;
import java.util.Iterator;

public class DetailFragment extends Fragment {

    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY = "detail_post";
    public static final String FAIL_MSG = "Failed to retrieve post";
    public static final String TAG = "DetailFragment";
    private static final String DATE_FORMAT = "MMMM dd, yyyy";

    ParseObject post;
    String postId;
    TextView tvUsername;
    TextView tvUsername2;
    TextView tvUsername3;
    TextView tvDescription;
    ImageView ivImage;
    ImageView ivProfilePicture;
    TextView tvCreatedAt;
    ImageView ivClose;

    public DetailFragment() {}

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        tvUsername = view.findViewById(R.id.tvUsername1);
        tvUsername2 = view.findViewById(R.id.tvUsername3);
        tvUsername3 = view.findViewById(R.id.tvUsername5);
        tvDescription = view.findViewById(R.id.tvDescription1);
        ivImage = view.findViewById(R.id.ivImage1);
        ivProfilePicture = view.findViewById(R.id.ivProfilePicture1);
        tvCreatedAt = view.findViewById(R.id.tvCreatedAt1);
        ivClose = view.findViewById(R.id.ivClose);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            postId = bundle.getString(KEY); // Key
        }

        readObject(postId);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new HomeFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_activity_main, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        return view;
    }

    public void readObject(String objectId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        query.include("description");
        query.include("image");
        query.include("createdAt");

        // The query will search for a ParseObject, given its objectId.
        // When the query finishes running, it will invoke the GetCallback
        // with either the object, or the exception thrown
        query.getInBackground(objectId, (object, e) -> {
            if (e == null) {

                post = object;

                ParseUser user = post.getParseUser("user");

                try {
                    user = user.fetchIfNeeded();
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                }

                /*Iterator<String> keys = user.keySet().iterator();

                while(keys.hasNext()) {
                    String key = keys.next();
                    Log.i(TAG, key);
                }*/

                Log.i(TAG, user.toString());
                tvUsername.setText(user.getString("username"));
                tvUsername2.setText(user.getString("username"));
                tvUsername3.setText(user.getString("username"));
                tvDescription.setText(post.getString(KEY_DESCRIPTION));
                tvCreatedAt.setText(calculateTimeAgo(post.getCreatedAt()));

                ParseFile image = post.getParseFile("image");
                int screenWidth = DeviceDimensionsHelper.getDisplayWidth(getContext());

                if (image != null) {
                    try {
                        Glide.with(getContext())
                                .load(image.getUrl())
                                .override(screenWidth, screenWidth)
                                .centerCrop()
                                .into(ivImage);
                    } catch (Exception i) {
                        i.printStackTrace();
                    }
                }

                ParseFile profileImage = user.getParseFile("image");
                if (profileImage != null) {
                    Glide.with(getContext())
                            .load(profileImage.getUrl())
                            .override(75, 75)
                            .circleCrop()
                            .into(ivProfilePicture);
                }
                    } else {
                        // something went wrong
                        Log.e(TAG, FAIL_MSG, e);
                    }
                });
    }

    public static String calculateTimeAgo(Date createdAt) {

        int SECOND_MILLIS = 1000;
        int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        int DAY_MILLIS = 24 * HOUR_MILLIS;

        try {
            createdAt.getTime();
            long time = createdAt.getTime();
            long now = System.currentTimeMillis();

            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " m";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + " h";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else {
                return DateFormat.format(DATE_FORMAT, createdAt).toString();
            }
        } catch (Exception e) {
            Log.i("Error:", "getRelativeTimeAgo failed", e);
            e.printStackTrace();
        }

        return "";
    }

}