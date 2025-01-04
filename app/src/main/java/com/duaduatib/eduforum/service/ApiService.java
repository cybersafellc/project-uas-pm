package com.duaduatib.eduforum.service;

import com.duaduatib.eduforum.model.Authentikasi;
import com.duaduatib.eduforum.model.Dosen;
import com.duaduatib.eduforum.model.Mahasiswa;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @POST("/users/mahasiswa")
    Call<Mahasiswa> registerMahasiswa(@Body Mahasiswa mahasiswa);

    @POST("/users/dosen")
    Call<Dosen> registerDosen(@Body Dosen dosen);

    @POST("/users/auth")
    Call<Authentikasi> authentikasiAuth(@Body Authentikasi authentikasi);

    @GET("/users/auth/verify")
    Call<Authentikasi> verifyAuth(@Path("access_token") Authentikasi verify);
}
