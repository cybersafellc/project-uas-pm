package com.duaduatib.eduforum;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.duaduatib.eduforum.RoomORM.AppDatabase;
import com.duaduatib.eduforum.RoomORM.Token;
import com.duaduatib.eduforum.RoomORM.TokenDao;

import com.duaduatib.eduforum.model.ApiResponse;

import com.duaduatib.eduforum.model.Authentikasi;
import com.duaduatib.eduforum.service.ApiService;
import com.duaduatib.eduforum.service.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class login_page extends AppCompatActivity {

    // Deklarasi komponen
    private Button btnLogin;
    private TextView textRegister;
    private EditText editUsername, editPassword;

    Retrofit retrofit;
    ApiService apiService;
    TokenDao tokenDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        // Inisialisasi komponen
        btnLogin = findViewById(R.id.btnLogin2);
        textRegister = findViewById(R.id.daftarAkunTxt2);
        editUsername = findViewById(R.id.inputUsername);
        editPassword = findViewById(R.id.inputPassword);

        retrofit = ApiClient.getRetrofitInstance();
        apiService = retrofit.create(ApiService.class);

        AppDatabase db = AppDatabase.getDatabase(this);
        tokenDao = db.tokenDao();

        // Aksi ketika tombol Login ditekan
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Authentikasi login = new Authentikasi();
                login.setUsername(editUsername.getText().toString());
                login.setPassword(editPassword.getText().toString());

                if (!validateInput(login)) {
                    Toast.makeText(login_page.this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.d("LoginData", "Username: " + login.getUsername());
                Log.d("LoginData", "Password: " + login.getPassword());


                Log.d("Login", "Mengirim data ke API: " + login.getUsername());

                // Melakukan POST data ke API autentikasi
                Call<Authentikasi> call = apiService.authentikasiAuth(login);
                call.enqueue(new Callback<Authentikasi>() {
                    @Override
                    public void onResponse(Call<Authentikasi> call, Response<Authentikasi> response) {
                        Log.d("Login", "Response diterima: " + response.toString());

                        if (response.isSuccessful() && response.body() != null) {
                            String accessToken = response.body().getData().getAccess_Token();

                            Log.d("Login", "Token diterima: " + accessToken);

                            // Simpan token ke local storage menggunakan Room ORM
                            Token token = new Token(accessToken);
                            new Thread(() -> {
                                tokenDao.deleteAllTokens(); // Hapus token lama
                                tokenDao.insert(token); // Simpan token baru

                                Token savedToken = tokenDao.getLastToken();
                                Log.d("SaveToken", "Token tersimpan: " + savedToken.getAccessToken());

                                // Verifikasi token ke API
                                runOnUiThread(() -> validateToken(accessToken));
                            }).start();
                        } else {
                            Log.e("Login", "Login gagal: " + response.message());
                            Toast.makeText(login_page.this, "Login gagal: " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Authentikasi> call, Throwable t) {
                        Log.e("Login", "Error: " + t.getMessage(), t);
                        Toast.makeText(login_page.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // Aksi ketika teks Daftar ditekan
        textRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), choose_role_page.class);
                startActivity(i);
            }
        });
    }

    private void validateToken(String token) {
        Log.d("VerifyToken", "Memulai verifikasi token: " + token);
        if (token == null || token.isEmpty()) {
            Log.e("VerifyToken", "Token tidak valid.");
            return;
        }
        Call<ApiResponse> call = apiService.validateToken("Bearer " + token);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.d("VerifyToken", "Response diterima: " + response.toString());
                if (response.isSuccessful()) {
                    Toast.makeText(login_page.this, "Login berhasil!", Toast.LENGTH_SHORT).show();

                    // Navigasi ke halaman berikutnya
                    Intent intent = new Intent(login_page.this, beranda.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("VerifyToken", "Verifikasi token gagal: " + response.message());
                    Toast.makeText(login_page.this, "Verifikasi token gagal: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("VerifyToken", "Error: " + t.getMessage(), t);
                Toast.makeText(login_page.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Boolean validateInput(Authentikasi login) {
        return !login.getUsername().isEmpty() &&
                !login.getPassword().isEmpty();
    }
}
