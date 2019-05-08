package com.example.sdwan.NetworkClient;

import com.example.sdwan.utils.Constant;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkCall {
    public static GetApi hitNetwork(){
        return new Retrofit.Builder().baseUrl(Constant.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build().create(GetApi.class);
    }
}
