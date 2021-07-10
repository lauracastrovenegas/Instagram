package com.example.instagram.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagram.MainActivity;
import com.example.instagram.R;
import com.example.instagram.DeviceDimensionsHelper;
import com.example.instagram.models.Post;
import com.example.instagram.ui.DetailFragment;
import com.example.instagram.ui.home.HomeFragment;
import com.parse.ParseFile;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    public static final String TAG = "PostAdapter";
    Context context;
    List<Post> posts;

    public PostAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @NotNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PostAdapter.ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvUsername;
        TextView tvDescription;
        ImageView ivImage;
        TextView tvUsername2;
        ImageView ivProfilePicture;
        TextView tvCreatedAt;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvUsername2 = itemView.findViewById(R.id.tvUsername2);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivImage = itemView.findViewById(R.id.ivImage);
            ivProfilePicture = itemView.findViewById(R.id.ivProfilePicture);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
        }

        public void bind(Post post) {
            tvUsername.setText(post.getUser().getUsername());
            tvUsername2.setText(post.getUser().getUsername());
            tvDescription.setText(post.getDescription());
            tvCreatedAt.setText(post.getCreatedAtDate(post));

            ParseFile image = post.getImage();
            int screenWidth = DeviceDimensionsHelper.getDisplayWidth(context);

            if (image != null) {
                try {
                    Glide.with(context)
                            .load(image.getUrl())
                            .override(screenWidth, screenWidth)
                            .centerCrop()
                            .into(ivImage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            ParseFile profileImage = post.getProfileImage();
            if (profileImage != null) {
                Glide.with(context)
                        .load(profileImage.getUrl())
                        .override(75, 75)
                        .circleCrop()
                        .into(ivProfilePicture);
            }
        }
    }
}

