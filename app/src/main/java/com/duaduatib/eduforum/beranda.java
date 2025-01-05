package com.duaduatib.eduforum;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.duaduatib.eduforum.R;

import com.duaduatib.eduforum.RoomORM.AppDatabase;
import com.duaduatib.eduforum.RoomORM.Token;
import com.duaduatib.eduforum.RoomORM.TokenDao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duaduatib.eduforum.model.ApiResponse;
import com.duaduatib.eduforum.model.Post;
import com.duaduatib.eduforum.service.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class beranda extends AppCompatActivity {

    private Retrofit retrofit;
    private ApiService apiService;

    private ImageView btnMenu;
    private RecyclerView recyclerViewPosts;
    private PostAdapter postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beranda);

        recyclerViewPosts = findViewById(R.id.recyclerViewPosts);
        recyclerViewPosts.setLayoutManager(new LinearLayoutManager(this));

        btnMenu = findViewById(R.id.btnMenu);

        // Dropdown menu setup
        btnMenu.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(beranda.this, btnMenu);
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.menu_dropdown, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> handleMenuClick(item));
            popupMenu.show();
        });

        fetchPosts();
    }

    private void fetchPosts() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://eduforumapi.htp22tib.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
        Call<ApiResponse> call = apiService.getPosts(1000);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    List<Post> posts = response.body().getData().getPosts();
                    if (posts != null && !posts.isEmpty()) {
                        postAdapter = new PostAdapter(posts, post -> {
                            Intent intent = new Intent(beranda.this, DetailPostActivity.class);
                            intent.putExtra("post_id", post.getId());
                            startActivity(intent);
                        });
                        recyclerViewPosts.setAdapter(postAdapter);
                    } else {
                        Toast.makeText(beranda.this, "No posts found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(beranda.this, "Failed to load posts", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Toast.makeText(beranda.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean handleMenuClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_profile) {
            Intent intent = new Intent(beranda.this, profil_user.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_add_post) {
            return true;
        } else if (id == R.id.menu_logout) {
            logout();
            return true;
        } else {
            return false;
        }
    }

    private void logout() {
        // Hapus token dari Room Database
        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
            TokenDao tokenDao = db.tokenDao();

            // Hapus semua token
            tokenDao.deleteAllTokens();
            Log.d("RoomDB", "Semua token dihapus dari database");

            // Arahkan pengguna ke halaman Welcome Page
            runOnUiThread(() -> {
                Toast.makeText(beranda.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(beranda.this, welcome_page.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            });
        }).start();
    }
}
