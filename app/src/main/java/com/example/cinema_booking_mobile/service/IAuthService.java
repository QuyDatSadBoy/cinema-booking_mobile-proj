package com.example.cinema_booking_mobile.service;

import com.example.cinema_booking_mobile.dto.request.LoginRequest;
import com.example.cinema_booking_mobile.dto.request.SignupRequest;
import com.example.cinema_booking_mobile.dto.response.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface IAuthService {

    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("/auth/register")
    Call<Object> register(@Body SignupRequest signUpRequest);

    @POST("/auth/refresh")
    Call<Object> refreshToken(@Header("Authorization") String refreshToken);

    @POST("/auth/logout")
    Call<Object> logout(@Header("Authorization") String token);
}