package com.duaduatib.eduforum;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class login_page extends AppCompatActivity {

    // Deklarasi komponen
    private Button btnLogin;
    private TextView textRegister;
    private EditText editUsername, editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        // Inisialisasi komponen
        btnLogin = findViewById(R.id.btnLogin2);
        textRegister = findViewById(R.id.daftarAkunTxt2);
        editUsername = findViewById(R.id.inputUsername);
        editPassword = findViewById(R.id.inputPassword);

        // Aksi ketika tombol Login ditekan
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = editUsername.getText().toString();
                final String password = editPassword.getText().toString();
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(login_page.this, "Isi semua field", Toast.LENGTH_SHORT).show();
                } else {
                    // Perform query
                    DatabaseEduForum databaseEduForum = DatabaseEduForum.getDatabaseEduForum(getApplicationContext());
                    final DosenDao dosenDao = databaseEduForum.dosenDao();
                    final MhsDao mhsDao = databaseEduForum.mhsDao(); // Tambahkan mhsDao

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // Cek login Dosen
                            EntitasDosen entitasDosen = dosenDao.login(username, password);
                            if (entitasDosen == null) {
                                // Jika username/password tidak cocok di Dosen, cek di MhsDao
                                EntitasMhs entitasMhs = mhsDao.login(username, password);
                                if (entitasMhs == null) {
                                    // Jika juga tidak ditemukan di MhsDao
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(login_page.this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    // Jika cocok di MhsDao (Mahasiswa)
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(login_page.this, "Login Mahasiswa Berhasil!", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(login_page.this, beranda.class));
                                        }
                                    });
                                }
                            } else {
                                // Jika cocok di DosenDao
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(login_page.this, "Login Dosen Berhasil!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(login_page.this, beranda.class));
                                    }
                                });
                            }
                        }
                    }).start();
                }
            }
        });

        // Aksi ketika teks Daftar ditekan
        textRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(login_page.this, "Navigate to Registration", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(),choose_role_page.class);
                startActivity(i);
            }
        });
    }
}
