package com.duaduatib.eduforum;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class post_text extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView btnBack, profileIcon;
    private TextView postTitle, postContent, likeCount, commentCount;
    private EditText commentInput;
    private ImageButton sendButton;
    private RecyclerView commentsRecyclerView;

    private List<String> commentsList;
    private CommentsAdapter commentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_text);

        // Initialize views
        toolbar = findViewById(R.id.toolbar);
        btnBack = findViewById(R.id.btnBack);
        profileIcon = findViewById(R.id.profileIcon);
        postTitle = findViewById(R.id.postTitle);
        postContent = findViewById(R.id.postContent);
        likeCount = findViewById(R.id.likeCount);
        commentCount = findViewById(R.id.commentCount);
        commentInput = findViewById(R.id.commentInput);
        sendButton = findViewById(R.id.sendButton);
        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);

        // Set up toolbar
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // Back button click listener
        btnBack.setOnClickListener(v -> finish());

        // Dummy data for post
        postTitle.setText("Ini adalah judul dari pertanyaan...");
        postContent.setText("Ini adalah pertanyaan yang diajukan oleh user...");
        likeCount.setText("823 Likes");
        commentCount.setText("142 Comments");

        // Set up comments RecyclerView
        commentsList = new ArrayList<>();
        commentsAdapter = new CommentsAdapter(commentsList);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentsRecyclerView.setAdapter(commentsAdapter);

        // Send button click listener
        sendButton.setOnClickListener(v -> {
            String comment = commentInput.getText().toString().trim();
            if (!comment.isEmpty()) {
                commentsList.add(comment);
                commentsAdapter.notifyItemInserted(commentsList.size() - 1);
                commentsRecyclerView.smoothScrollToPosition(commentsList.size() - 1);
                commentInput.setText("");
                Toast.makeText(this, "Comment added!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Comment cannot be empty!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
