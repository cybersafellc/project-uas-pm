package com.duaduatib.eduforum;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.duaduatib.eduforum.model.Post;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private final List<Post> posts;
    private final OnPostClickListener onPostClickListener;

    public interface OnPostClickListener {
        void onPostClick(Post post);
    }

    public PostAdapter(List<Post> posts, OnPostClickListener listener) {
        this.posts = posts;
        this.onPostClickListener = listener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;
        private final TextView contentTextView;
        private final TextView usernameTextView;
        private final TextView likeCountTextView;
        private final TextView commentCountTextView;
        private final TextView roleTextView;
        private final ImageView postImageView;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            roleTextView = itemView.findViewById(R.id.roleTextView);
            postImageView = itemView.findViewById(R.id.postImageView);
            likeCountTextView = itemView.findViewById(R.id.likeCountTextView);
            commentCountTextView = itemView.findViewById(R.id.commentCountTextView);
        }

        public void bind(Post post) {
            // Bind data to views
            titleTextView.setText(post.getTitle());
            contentTextView.setText(post.getContent());
            usernameTextView.setText(post.getUser().getFull_name());
            roleTextView.setText(post.getUser().getRole());
            likeCountTextView.setText(post.getCount_like() + " likes");
            commentCountTextView.setText(post.getCount_answers() + " comments");

            // Load image using Glide
            Glide.with(itemView.getContext())
                    .load(post.getImg_url())
                    .into(postImageView);

            // Set click listener on item view
            itemView.setOnClickListener(v -> {
                if (onPostClickListener != null) {
                    onPostClickListener.onPostClick(post);
                }
            });
        }
    }
}
