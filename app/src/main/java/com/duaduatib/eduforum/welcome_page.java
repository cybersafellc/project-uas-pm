package com.duaduatib.eduforum;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.duaduatib.eduforum.RoomORM.AppDatabase;
import com.duaduatib.eduforum.RoomORM.Token;
import com.duaduatib.eduforum.RoomORM.TokenDao;

import com.duaduatib.eduforum.model.ApiResponse;
import com.duaduatib.eduforum.model.IdTokenRequest;
import com.duaduatib.eduforum.model.Data;

import com.duaduatib.eduforum.service.ApiService;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class welcome_page extends AppCompatActivity {

    Retrofit retrofit;
    ApiService apiService;

    private static final int REQ_ONE_TAP = 9001;
    private SignInClient oneTapClient;
    private FirebaseAuth firebaseAuth;

    // Deklarasi komponen
    private Button btnLogin, btnLoginGoogle;
    private TextView textRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);

        // Inisialisasi Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        oneTapClient = Identity.getSignInClient(this);

        // Inisialisasi komponen
        btnLogin = findViewById(R.id.btnLogin);
        btnLoginGoogle = findViewById(R.id.btnLoginGoogle);
        textRegister = findViewById(R.id.daftarAkunTxt);

        // Cek token di database
        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
            TokenDao tokenDao = db.tokenDao();

            Token token = tokenDao.getLastToken();
            if (token != null) {
                Log.d("RoomDB", "Token ditemukan di database: " + token.getAccessToken());

                // Validasi token ke API
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://eduforumapi.htp22tib.com")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ApiService apiService = retrofit.create(ApiService.class);

                Call<ApiResponse> call = apiService.validateToken("Bearer " + token.getAccessToken());
                call.enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Log.d("Token Validation", "Token valid. Mengarahkan ke beranda.");
                            runOnUiThread(() -> {
                                Intent intent = new Intent(welcome_page.this, beranda.class);
                                startActivity(intent);
                                finish(); // Tutup halaman ini
                            });
                        } else {
                            Log.d("Token Validation", "Token tidak valid. Harus login ulang.");
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Log.e("Token Validation", "Validasi token gagal: " + t.getMessage());
                    }
                });

            } else {
                Log.d("RoomDB", "Tidak ada token yang tersimpan. Pengguna harus login.");
            }
        }).start();

        // Aksi ketika tombol Login ditekan
        btnLogin.setOnClickListener(v -> {

            Intent i = new Intent(getApplicationContext(), login_page.class);
            startActivity(i);
        });

        // Aksi ketika tombol Login dengan Google ditekan
        btnLoginGoogle.setOnClickListener(v -> startGoogleSignIn());

        // Aksi ketika teks Daftar ditekan
        textRegister.setOnClickListener(v -> {

            Intent i = new Intent(getApplicationContext(), choose_role_page.class);
            startActivity(i);
        });
    }

    private void startGoogleSignIn() {
        BeginSignInRequest signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(getString(R.string.default_web_client_id))
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();

        oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(this, result -> {
                    try {
                        startIntentSenderForResult(result.getPendingIntent().getIntentSender(),
                                REQ_ONE_TAP, null, 0, 0, 0, null);
                    } catch (Exception e) {
                        Log.e("SignInError", "Error starting intent", e);
                    }
                })
                .addOnFailureListener(this, e -> {
                    Toast.makeText(welcome_page.this, "Google Sign-In failed", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_ONE_TAP) {
            try {
                SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                String googleIdToken = credential.getGoogleIdToken();

                if (googleIdToken != null) {
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null);

                    firebaseAuth.signInWithCredential(firebaseCredential)
                            .addOnCompleteListener(this, task -> {
                                if (task.isSuccessful()) {
                                    // Dapatkan Firebase ID Token
                                    FirebaseUser user = firebaseAuth.getCurrentUser();

                                    // Retrofit instance
                                    Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl("https://eduforumapi.htp22tib.com")
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();

                                    if (user != null) {
                                        user.getIdToken(true).addOnCompleteListener(tokenTask -> {
                                            if (tokenTask.isSuccessful()) {
                                                // Ini adalah Firebase ID Token
                                                String firebaseIdToken = tokenTask.getResult().getToken();
                                                Log.d("FirebaseIDToken", firebaseIdToken);

                                                // API service
                                                ApiService apiService = retrofit.create(ApiService.class);

                                                // Request body
                                                IdTokenRequest idTokenRequest = new IdTokenRequest(firebaseIdToken);

                                                // API call
                                                Call<ApiResponse> call = apiService.loginWithGoogle(idTokenRequest);
                                                call.enqueue(new Callback<ApiResponse>() {
                                                    @Override
                                                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                                        if (response.isSuccessful() && response.body() != null) {
                                                            // Menampilkan pesan sukses ke Logcat dan Toast
                                                            Log.d("API Response", "Success: " + response.body().getMessage());

                                                            // Ambil access token dari data respons
                                                            String accessToken = response.body().getData().getAccess_Token();
                                                            Log.d("Access Token", "Token: " + accessToken);

                                                            // Simpan access token ke database Room
                                                            new Thread(() -> {
                                                                AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
                                                                TokenDao tokenDao = db.tokenDao();

                                                                // Hapus token lama jika ada
                                                                tokenDao.deleteAllTokens();

                                                                // Simpan token baru
                                                                tokenDao.insert(new Token(accessToken));
                                                                Log.d("RoomDB", "Token disimpan ke database");
                                                            }).start();

                                                            // Ambil token dari database
                                                            new Thread(() -> {
                                                                AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
                                                                TokenDao tokenDao = db.tokenDao();

                                                                Token token = tokenDao.getLastToken();
                                                                if (token != null) {
                                                                    Log.d("RoomDB", "Token dari database: " + token.getAccessToken());
                                                                } else {
                                                                    Log.d("RoomDB", "Tidak ada token yang tersimpan");
                                                                }
                                                            }).start();

                                                            Toast.makeText(welcome_page.this, "Welcome " + user.getDisplayName(), Toast.LENGTH_SHORT).show();

                                                            // Navigasi ke halaman Beranda
                                                            Intent i = new Intent(getApplicationContext(), beranda.class);
                                                            startActivity(i);
                                                        } else {
                                                            Log.e("API Response", "Error: " + response.errorBody());
                                                            Toast.makeText(welcome_page.this, "Login Error: " + response.message(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                                                        Log.e("API Response", "Failure: " + t.getMessage());
                                                        Toast.makeText(welcome_page.this, "API Failure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                            } else {
                                                Log.e("TokenError", "Gagal mendapatkan Firebase ID Token", tokenTask.getException());
                                            }
                                        });
                                    }
                                } else {
                                    Toast.makeText(welcome_page.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                                    Log.e("AuthError", "Gagal autentikasi Firebase", task.getException());
                                }
                            });
                }
            } catch (ApiException e) {
                Log.e("SignInError", "SignIn failed", e);
            }
        }
    }
}