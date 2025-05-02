package com.example.cinema_booking_mobile.service;

import com.example.cinema_booking_mobile.dto.request.UserProfileUpdateRequest;
import com.example.cinema_booking_mobile.dto.response.AvatarResponse;
import com.example.cinema_booking_mobile.dto.response.MessageResponse;
import com.example.cinema_booking_mobile.model.UserProfile;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface IProfileService {
    @GET("/api/user/profile")
    Call<UserProfile> getUserProfile(@Header("Authorization") String token);

    @PUT("/api/user/profile")
    Call<MessageResponse> updateUserProfile(
            @Header("Authorization") String token,
            @Body UserProfileUpdateRequest request);

    @Multipart
    @POST("/api/user/avatar")
    Call<AvatarResponse> uploadAvatar(
            @Header("Authorization") String token,
            @Part MultipartBody.Part file);
}