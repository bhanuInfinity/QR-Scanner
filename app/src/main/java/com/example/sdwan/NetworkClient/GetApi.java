package com.example.sdwan.NetworkClient;

import android.support.annotation.MainThread;

import com.example.sdwan.model.CpeResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetApi {
    @GET("/clients/{id}")
    Call<CpeResponse> getCpeData(@Path("id") String id);
}
