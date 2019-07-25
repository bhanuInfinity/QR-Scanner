package com.infinitylabs.udwan.NetworkClient;

import com.infinitylabs.udwan.model.CpeActivationRequest;
import com.infinitylabs.udwan.model.CpeResponse;
import com.infinitylabs.udwan.model.Login.LoginRequest;
import com.infinitylabs.udwan.model.Login.LoginResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface GetApi {
    @GET("/api/devices/{id}")
    Call<CpeResponse> getCpeData(@HeaderMap Map<String,String> headers,@Path("id") String id);

    @POST("/api//devices")
    Call<CpeResponse> PostActivation(@HeaderMap Map<String,String> headers, @Body CpeActivationRequest cpeActivationRequest);


    @POST("/api/login")
    Call<LoginResponse> Login(@Body LoginRequest loginRequest);

    @GET("/api/licenses")
    Call<com.infinitylabs.udwan.model.dashboard.Response> getAllLicenses(@HeaderMap Map<String,String> header);

    @PUT("/api//devices")
    Call<CpeResponse> UpdateActivation(@HeaderMap Map<String,String> headers, @Body CpeActivationRequest cpeActivationRequest);
}
