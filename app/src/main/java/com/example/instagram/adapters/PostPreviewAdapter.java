package com.example.instagram.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagram.DeviceDimensionsHelper;
import com.example.instagram.R;
import com.example.instagram.models.Post;
import com.parse.ParseFile;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PostPreviewAdapter extends RecyclerView.Adapter<PostPreviewAdapter.ViewHolder>{

    public static final String TAG = "PostPreviewAdapter";
    Context context;
    List<Post> posts;

    public PostPreviewAdapter (Context context, List<Post> posts){
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @NotNull
    @Override
    public PostPreviewAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post_grid, parent, false);
        int height = parent.getMeasuredWidth() / 3;
        view.setMinimumHeight(height);
        return new PostPreviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PostPreviewAdapter.ViewHolder holder, int position) {
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

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivPostPreview;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            ivPostPreview = itemView.findViewById(R.id.ivPostPreview);
        }

        public void bind(Post post) {

            int screenWidth = DeviceDimensionsHelper.getDisplayWidth(context);
            ParseFile image = post.getImage();

            if (image != null) {
                try {
                    Glide.with(context)
                            .load(image.getUrl())
                            .override(screenWidth/3, screenWidth/3)
                            .centerCrop()
                            .into(ivPostPreview);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
