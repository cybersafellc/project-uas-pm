package com.duaduatib.eduforum.service;

import com.duaduatib.eduforum.model.Authentikasi;
import com.duaduatib.eduforum.model.Dosen;
import com.duaduatib.eduforum.model.IdTokenRequest;
import com.duaduatib.eduforum.model.Mahasiswa;
import com.duaduatib.eduforum.model.ApiResponse;
import com.duaduatib.eduforum.model.PostResponse;
import com.duaduatib.eduforum.model.ProfileResponse;
import com.duaduatib.eduforum.model.ResponseData;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

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

    @GET("posts")
    Call<ApiResponse> getPosts(@Query("take") int take);

    @GET("posts")
    Call<PostResponse> getPostDetail(@Query("id") String postId);

    @GET("posts/answers")
    Call<ResponseData> getPosts();

    @Multipart
    @POST("posts")
    Call<ApiResponse> createPost(
            @Header("Authorization") String authToken,
            @Part List<MultipartBody.Part> file,
            @Part("content") RequestBody content,
            @Part("title") RequestBody title
    );

}
