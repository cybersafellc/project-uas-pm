package com.duaduatib.eduforum;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class choose_role_page extends AppCompatActivity {

    // Deklarasi komponen
    private Button btnMahasiswa, btnDosen;
    private TextView punyaAkunTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_role_page);

        // Inisialisasi komponen
        btnMahasiswa = findViewById(R.id.btnMahasiswa);
        btnDosen = findViewById(R.id.btnDosen);
        punyaAkunTxt = findViewById(R.id.punyaAkunTxt);

        // Aksi ketika tombol Mahasiswa ditekan
        btnMahasiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(choose_role_page.this, "Mahasiswa Button Clicked", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(),daftar_mahasiswa_page.class);
                startActivity(i);
            }
        });

        // Aksi ketika tombol Dosen ditekan
        btnDosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(choose_role_page.this, "Dosen Button Clicked", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(),daftar_dosen_page.class);
                startActivity(i);
            }
        });

        // Aksi ketika teks Punya Akun ditekan
        punyaAkunTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(choose_role_page.this, "Navigate to Login Page", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(),login_page.class);
                startActivity(i);
            }
        });
    }
}
