package com.duaduatib.eduforum;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.duaduatib.eduforum.model.Mahasiswa;
import com.duaduatib.eduforum.service.ApiService;
import com.duaduatib.eduforum.service.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class daftar_mahasiswa_page extends AppCompatActivity {

    // Deklarasi komponen
    Retrofit retrofit;
    ApiService apiService;

    private Button btnDaftarMhs;
    private EditText usernameMhs, passwordMhs, namaLengkapMhs;
    private EditText NIM, perguruanTinggiMhs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daftar_mahasiswa_page);

        // Inisialisasi komponen
        btnDaftarMhs = findViewById(R.id.btnDaftarMhs);
        usernameMhs = findViewById(R.id.inputUsernameDaftarMhs);
        passwordMhs = findViewById(R.id.inputPasswordDaftarMhs);
        namaLengkapMhs = findViewById(R.id.inputNamaLengkapDaftarMhs);
        NIM = findViewById(R.id.inputNIMDaftarMhs);
        perguruanTinggiMhs = findViewById(R.id.inputPerguruanTinggiDaftarMhs);

        retrofit = ApiClient.getRetrofitInstance();
        apiService = retrofit.create(ApiService.class);

        // Aksi ketika tombol Daftar ditekan
        btnDaftarMhs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mahasiswa mahasiswa = new Mahasiswa();
                mahasiswa.setUsername(usernameMhs.getText().toString().trim());
                mahasiswa.setPassword(passwordMhs.getText().toString().trim());
                mahasiswa.setFull_name(namaLengkapMhs.getText().toString().trim());
                mahasiswa.setNidn_or_nim(NIM.getText().toString().trim());
                mahasiswa.setNama_perguruan_tinggi(perguruanTinggiMhs.getText().toString().trim());

                if (!validateInput(mahasiswa)) {
                    Toast.makeText(daftar_mahasiswa_page.this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Melakukan POST data
                Call<Mahasiswa> call = apiService.registerMahasiswa(mahasiswa);
                call.enqueue(new Callback<Mahasiswa>() {
                    @Override
                    public void onResponse(Call<Mahasiswa> call, Response<Mahasiswa> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(daftar_mahasiswa_page.this, "Pendaftaran berhasil!", Toast.LENGTH_SHORT).show();

                            // Navigasi ke halaman berikutnya jika perlu
                            Intent intent = new Intent(daftar_mahasiswa_page.this, login_page.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.d("Daftar", response.toString());
                            Toast.makeText(daftar_mahasiswa_page.this, "Pendaftaran gagal: " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Mahasiswa> call, Throwable t) {
                        Toast.makeText(daftar_mahasiswa_page.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private Boolean validateInput(Mahasiswa mahasiswa) {
        return !mahasiswa.getUsername().isEmpty() &&
                !mahasiswa.getPassword().isEmpty() &&
                !mahasiswa.getFull_name().isEmpty() &&
                !mahasiswa.getNidn_or_nim().isEmpty() &&
                !mahasiswa.getNama_perguruan_tinggi().isEmpty();
    }
}
