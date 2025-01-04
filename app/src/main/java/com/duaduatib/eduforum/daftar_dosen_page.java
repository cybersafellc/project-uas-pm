package com.duaduatib.eduforum;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.duaduatib.eduforum.model.Dosen;
import com.duaduatib.eduforum.model.Mahasiswa;
import com.duaduatib.eduforum.service.ApiService;
import com.duaduatib.eduforum.service.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class daftar_dosen_page extends AppCompatActivity {

    // Deklarasi komponen
    Retrofit retrofit;
    ApiService apiService;

    private Button btnDaftarDosen;
    private EditText usernameDosen, passwordDosen, namaLengkapDosen;
    private EditText NIDN, perguruanTinggiDosen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daftar_dosen_page);

        // Inisialisasi komponen
        btnDaftarDosen = findViewById(R.id.btnDaftarDosen);
        usernameDosen = findViewById(R.id.inputUsernameDaftarDosen);
        passwordDosen = findViewById(R.id.inputPasswordDaftarDosen);
        namaLengkapDosen = findViewById(R.id.inputNamaLengkapDaftarDosen);
        NIDN = findViewById(R.id.inputNIDNDaftarDosen);
        perguruanTinggiDosen = findViewById(R.id.inputPerguruanTinggiDaftarDosen);

        retrofit = ApiClient.getRetrofitInstance();
        apiService = retrofit.create(ApiService.class);

        // Aksi ketika tombol Daftar ditekan
        btnDaftarDosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Creating Dosen Entity
                Dosen dosen = new Dosen();
                dosen.setUsername(usernameDosen.getText().toString());
                dosen.setPassword(passwordDosen.getText().toString());
                dosen.setFull_name(namaLengkapDosen.getText().toString());
                dosen.setNidn_or_nim(NIDN.getText().toString());
                dosen.setNama_perguruan_tinggi(perguruanTinggiDosen.getText().toString());

                if (!validateInput(dosen)) {
                    Toast.makeText(daftar_dosen_page.this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Melakukan POST data
                Call<Dosen> call = apiService.registerDosen(dosen);
                call.enqueue(new Callback<Dosen>() {
                    @Override
                    public void onResponse(Call<Dosen> call, Response<Dosen> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(daftar_dosen_page.this, "Pendaftaran berhasil!", Toast.LENGTH_SHORT).show();

                            // Navigasi ke halaman berikutnya jika perlu
                            Intent intent = new Intent(daftar_dosen_page.this, login_page.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(daftar_dosen_page.this, "Pendaftaran gagal: " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Dosen> call, Throwable t) {
                        Toast.makeText(daftar_dosen_page.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    private Boolean validateInput(Dosen dosen) {
        return !dosen.getUsername().isEmpty() &&
                !dosen.getPassword().isEmpty() &&
                !dosen.getFull_name().isEmpty() &&
                !dosen.getNidn_or_nim().isEmpty() &&
                !dosen.getNama_perguruan_tinggi().isEmpty();
    }
}