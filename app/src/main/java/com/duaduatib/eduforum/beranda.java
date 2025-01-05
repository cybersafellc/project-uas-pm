package com.duaduatib.eduforum;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class beranda extends AppCompatActivity {

    private ImageButton tombolProfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beranda);

        // Menghubungkan tombolProfil dengan elemen di layout
        tombolProfil = findViewById(R.id.btnProfil);

        // Menambahkan listener untuk tombolProfil
        tombolProfil.setOnClickListener(v -> {
            // Berpindah ke halaman profile
            Intent intent = new Intent(beranda.this, profil_user.class);
            startActivity(intent);
        });
    }
}