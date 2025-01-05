package com.duaduatib.eduforum;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.duaduatib.eduforum.model.Comment;
import com.duaduatib.eduforum.model.Post;
import com.duaduatib.eduforum.model.PostResponse;
import com.duaduatib.eduforum.service.ApiService;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailPostActivity extends AppCompatActivity {

    private RecyclerView commentsRecyclerView;
    private CommentsAdapter commentsAdapter;
    private List<Comment> commentList = new ArrayList<>();


    private TextView titleTextView, contentTextView, usernameTextView, roleTextView, likeCountTextView, commentCountTextView;
    private ImageView postImageView, profileImageView;
    private ImageButton exitButton; // Tambahkan variabel untuk tombol keluar

    private Retrofit retrofit;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);

        // Inisialisasi RecyclerView
        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);
        commentsAdapter = new CommentsAdapter(commentList);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentsRecyclerView.setAdapter(commentsAdapter);

        // Initialize views
        titleTextView = findViewById(R.id.titleTextView);
        contentTextView = findViewById(R.id.contentTextView);
        usernameTextView = findViewById(R.id.usernameTextView);
        roleTextView = findViewById(R.id.roleTextView);
        likeCountTextView = findViewById(R.id.likeCountTextView);
        commentCountTextView = findViewById(R.id.commentCountTextView);
        postImageView = findViewById(R.id.postImageView);
        profileImageView = findViewById(R.id.profileImageView);
        exitButton = findViewById(R.id.exitButton); // Inisialisasi tombol keluar

        // Handle exit button click
        exitButton.setOnClickListener(view -> {
            Intent intent = new Intent(DetailPostActivity.this, beranda.class); // MainActivity adalah halaman beranda
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        // Get the post ID from intent extras
        String postId = getIntent().getStringExtra("post_id");
        if (postId == null) {
            Toast.makeText(this, "Post ID not provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Set up Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl("https://eduforumapi.htp22tib.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        // Fetch comments
        fetchComments(postId);

        // Fetch post details
        fetchPostDetails(postId);
    }

    private void fetchPostDetails(String postId) {
        Call<PostResponse> call = apiService.getPostDetail(postId);

        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(@NonNull Call<PostResponse> call, @NonNull Response<PostResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Post post = response.body().getData(); // Ambil objek Post dari respons
                    Log.d("Post Debug", "Post: " + new Gson().toJson(post));
                    populatePostDetails(post);
                } else {
                    Log.e("API Error", "Response unsuccessful or body null");
                }
            }

            @Override
            public void onFailure(@NonNull Call<PostResponse> call, @NonNull Throwable t) {
                Log.e("API Error", "Failure: " + t.getMessage());
            }
        });
    }

    private void populatePostDetails(Post post) {
        titleTextView.setText(post.getTitle() != null ? post.getTitle() : "No title");
        contentTextView.setText(post.getContent() != null ? post.getContent() : "No content");

        if (post.getUser() != null) {
            usernameTextView.setText(post.getUser().getFull_name() != null ? post.getUser().getFull_name() : "Unknown user");
            roleTextView.setText(post.getUser().getRole() != null ? post.getUser().getRole() : "Unknown role");

            if (post.getUser().getProfile_url() != null) {
                Glide.with(this)
                        .load(post.getUser().getProfile_url())
                        .into(profileImageView);
            }
        } else {
            usernameTextView.setText("Unknown user");
            roleTextView.setText("Unknown role");
        }

        if (post.getImg_url() != null) {
            Glide.with(this)
                    .load(post.getImg_url())
                    .into(postImageView);
        } else {
            postImageView.setImageResource(R.drawable.placeholder_image);
        }

        likeCountTextView.setText(post.getCount_like() + " likes");
        commentCountTextView.setText(post.getCount_answers() + " comments");
    }

    private void fetchComments(String postId) {
        Call<List<Comment>> call = apiService.getPostComments(postId);

        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(@NonNull Call<List<Comment>> call, @NonNull Response<List<Comment>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    commentList.clear();
                    commentList.addAll(response.body());
                    commentsAdapter.notifyDataSetChanged();
                } else {
                    Log.e("API Error", "Unable to fetch comments");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Comment>> call, @NonNull Throwable t) {
                Log.e("API Error", "Failure: " + t.getMessage());
            }
        });
    }
}
