package com.example.sdwan.NetworkClient;

import android.support.annotation.MainThread;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetApi {
    @GET("")
    Call<MainThread> getData();
}
