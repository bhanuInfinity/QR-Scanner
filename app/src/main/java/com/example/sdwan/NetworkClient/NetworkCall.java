package com.example.sdwan.NetworkClient;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkCall {
    public static GetApi hitNetwork(){
        return new Retrofit.Builder().baseUrl("").addConverterFactory(GsonConverterFactory.create()).build().create(GetApi.class);
    }
}
