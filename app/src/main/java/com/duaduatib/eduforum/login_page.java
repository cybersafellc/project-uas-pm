package com.duaduatib.eduforum;

import static com.duaduatib.eduforum.service.ApiClient.retrofit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.duaduatib.eduforum.model.Authentikasi;
import com.duaduatib.eduforum.model.Dosen;
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
                // Melakukan POST data
                Call<Authentikasi> call = apiService.authentikasiAuth(login);
                call.enqueue(new Callback<Authentikasi>() {
                    @Override
                    public void onResponse(Call<Authentikasi> call, Response<Authentikasi> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(login_page.this, "Login berhasil!", Toast.LENGTH_SHORT).show();

                            // Navigasi ke halaman berikutnya jika perlu
                            Intent intent = new Intent(login_page.this, beranda.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(login_page.this, "Login gagal: " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Authentikasi> call, Throwable t) {
                        Toast.makeText(login_page.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // Aksi ketika teks Daftar ditekan
        textRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(),choose_role_page.class);
                startActivity(i);
            }
        });
    }

    private Boolean validateInput(Authentikasi login) {
        return !login.getUsername().isEmpty() &&
                !login.getPassword().isEmpty();
    }
}
