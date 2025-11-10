package com.example.app.network;

import com.example.app.dto.LoginRequest;
import com.example.app.dto.LoginResponse;
import com.example.app.network.model.RegisterRequest;
import com.example.app.network.model.RegisterResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {
    @POST("api/register")
    Call<RegisterResponse> register(@Body RegisterRequest request);

    @POST("/api/login")
    Call<String> login(@Body LoginRequest request);
}
