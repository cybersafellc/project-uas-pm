package com.duaduatib.eduforum;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.duaduatib.eduforum.RoomORM.AppDatabase;
import com.duaduatib.eduforum.RoomORM.Token;
import com.duaduatib.eduforum.RoomORM.TokenDao;

import com.duaduatib.eduforum.model.ProfileResponse;
import com.duaduatib.eduforum.service.ApiService;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class profil_user extends AppCompatActivity {

    private EditText username, fullName, studentId, university;
    private ImageView profileImage;
    private ImageButton btnBack;
    private Button btnSimpanEditProfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil_user);

        // Inisialisasi komponen
        username = findViewById(R.id.editUsername);
        fullName = findViewById(R.id.editNamaLengkap);
        studentId = findViewById(R.id.editNIM);
        university = findViewById(R.id.editAsalPT);
        profileImage = findViewById(R.id.fotoProfil);
        btnBack = findViewById(R.id.backButton);
        btnSimpanEditProfil = findViewById(R.id.btnSimpanEditProfil);

        // Membuat kolom username non-editable
        username.setEnabled(false); // Alternatif: username.setFocusable(false);

        btnBack.setOnClickListener(v -> {
            finish(); // Tutup halaman ini untuk kembali ke halaman sebelumnya (beranda)
        });

        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
        // Ambil access token dari database
        new Thread(() -> {
            TokenDao tokenDao = db.tokenDao();

            Token token = tokenDao.getLastToken();
            if (token != null) {
                Log.d("RoomDB", "Token dari database: " + token.getAccessToken());
                runOnUiThread(() -> fetchUserProfile(token.getAccessToken())); // Panggil fetchUserProfile dengan token

                btnSimpanEditProfil.setOnClickListener(v -> {
                    String fullNameValue = fullName.getText().toString();
                    String studentIdValue = studentId.getText().toString();
                    String universityValue = university.getText().toString();

                    updateUserProfile(token.getAccessToken(), fullNameValue, studentIdValue, universityValue);
                });

            } else {
                Log.d("RoomDB", "Tidak ada token yang tersimpan");
                runOnUiThread(() -> Toast.makeText(profil_user.this, "Token tidak ditemukan", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void fetchUserProfile(String accessToken) {

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();
                    Request.Builder builder = originalRequest.newBuilder()
                            .header("Authorization", "Bearer " + accessToken);
                    Request newRequest = builder.build();

                    Log.d("Interceptor", "Request URL: " + newRequest.url());
                    Log.d("Interceptor", "Request Headers: " + newRequest.headers());

                    return chain.proceed(newRequest);
                })
                .build();

        // Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://eduforumapi.htp22tib.com")
                .client(client) // Tambahkan OkHttpClient ke Retrofit
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // API service
        ApiService apiService = retrofit.create(ApiService.class);

        // API call
        Call<ProfileResponse> call = apiService.getUserProfile();
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Update UI dengan data dari API
                    ProfileResponse profile = response.body();
                    Log.d("ProfileActivity", "Profile Loaded: " + profile.getData().getUsername());
                    username.setText(profile.getData().getUsername());
                    fullName.setText(profile.getData().getFull_name());
                    studentId.setText(profile.getData().getNidn_or_nim());
                    university.setText(profile.getData().getNama_perguruan_tinggi());

                    Picasso.get()
                            .load(profile.getData().getProfile_url())
                            .placeholder(R.drawable.ic_placeholder) // Placeholder saat loading
                            .error(R.drawable.ic_error) // Gambar error jika gagal memuat
                            .transform(new CropCircleTransformation()) // Transformasi ke bentuk lingkaran
                            .into(profileImage);
                } else {
                    Log.e("ProfileActivity", "API Error: " + response.code() + " - " + response.message());
                    Toast.makeText(profil_user.this, "Gagal mendapatkan data profil", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Log.e("ProfileActivity", "Error: " + t.getMessage());
                Toast.makeText(profil_user.this, "Terjadi kesalahan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserProfile(String accessToken, String fullNameValue, String studentIdValue, String universityValue) {

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();
                    Request.Builder builder = originalRequest.newBuilder()
                            .header("Authorization", "Bearer " + accessToken);
                    Request newRequest = builder.build();

                    return chain.proceed(newRequest);
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://eduforumapi.htp22tib.com")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        // Membuat body request dalam format JSON
        JSONObject requestBodyJson = new JSONObject();
        try {
            requestBodyJson.put("full_name", fullNameValue);
            requestBodyJson.put("nidn_or_nim", studentIdValue);
            requestBodyJson.put("nama_perguruan_tinggi", universityValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestBodyJson.toString());

        // API call untuk update profil
        Call<ResponseBody> call = apiService.updateUserProfile(
                "Bearer " + accessToken,
                requestBody
        );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(profil_user.this, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("UpdateProfile", "Gagal memperbarui profil: " + response.code() + " - " + response.message());
                    Toast.makeText(profil_user.this, "Gagal memperbarui profil", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("UpdateProfile", "Kesalahan: " + t.getMessage());
                Toast.makeText(profil_user.this, "Terjadi kesalahan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
