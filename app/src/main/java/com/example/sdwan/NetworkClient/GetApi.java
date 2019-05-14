package com.example.sdwan.NetworkClient;

import android.support.annotation.MainThread;

import com.example.sdwan.model.CpeActivationRequest;
import com.example.sdwan.model.CpeResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetApi {
    @GET("/clients/{id}")
    Call<CpeResponse> getCpeData(@Path("id") String id);

    @POST("/clients")
    Call<CpeResponse> PostActivation(@Body CpeActivationRequest cpeActivationRequest);

    @PUT("/clients")
    Call<CpeResponse> updateActivation(@Body CpeActivationRequest cpeActivationRequest);
}
