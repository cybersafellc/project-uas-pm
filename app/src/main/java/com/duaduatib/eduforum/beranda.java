package com.duaduatib.eduforum;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

public class beranda extends AppCompatActivity {

    private ImageButton tombolProfil;

    private SearchView tombolSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beranda);

        // Menghubungkan tombolProfil dengan elemen di layout
        tombolProfil = findViewById(R.id.btnProfil);
        tombolSearch = findViewById(R.id.tombolSearch);

        tombolSearch.clearFocus();

        tombolSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                tombolSearch.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        // Menambahkan listener untuk tombolProfil
        tombolProfil.setOnClickListener(v -> {
            // Berpindah ke halaman profile
            Intent intent = new Intent(beranda.this, profil_user.class);
            startActivity(intent);
        });
    }
}