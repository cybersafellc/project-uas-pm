package com.duaduatib.eduforum.service;

import com.duaduatib.eduforum.model.Authentikasi;
import com.duaduatib.eduforum.model.Dosen;
import com.duaduatib.eduforum.model.IdTokenRequest;
import com.duaduatib.eduforum.model.Mahasiswa;
import com.duaduatib.eduforum.model.ApiResponse;
import com.duaduatib.eduforum.model.ProfileResponse;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ApiService {
    @POST("/users/mahasiswa")
    Call<Mahasiswa> registerMahasiswa(@Body Mahasiswa mahasiswa);

    @POST("/users/dosen")
    Call<Dosen> registerDosen(@Body Dosen dosen);

    @POST("/users/auth")
    Call<Authentikasi> authentikasiAuth(@Body Authentikasi authentikasi);

    @POST("/users/oauth/google")
    Call<ApiResponse> loginWithGoogle(@Body IdTokenRequest request);

    @GET("/users/profile")
    Call<ProfileResponse> getUserProfile();

    @GET("/users/auth/verify")
    Call<ApiResponse> validateToken(@Header("Authorization") String token);

    @PUT("users/profile")
    Call<ResponseBody> updateUserProfile(
            @Header("Authorization") String authorization,
            @Body RequestBody requestBody
    );
}
