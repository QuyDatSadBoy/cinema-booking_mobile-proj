package com.example.cinema_booking_mobile.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface IUserService {

    @GET("/user/profile")
    Call<Object> getUserProfile(@Header("Authorization") String token);

    @GET("/user/check")
    Call<Object> checkAuthentication(@Header("Authorization") String token);

    @GET("/user/{id}")
    Call<Object> getUserById(@Header("Authorization") String token, @Path("id") Integer id);
}