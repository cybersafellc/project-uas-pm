package com.duaduatib.eduforum.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static Retrofit retrofit;
    public static final String BASE_URL = "https://eduforumapi.htp22tib.com";

    public static Retrofit getRetrofitInstance(){
        if (retrofit == null){
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
