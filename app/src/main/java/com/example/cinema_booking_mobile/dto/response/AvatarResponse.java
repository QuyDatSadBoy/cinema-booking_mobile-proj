package com.example.cinema_booking_mobile.dto.response;

import com.google.gson.annotations.SerializedName;

public class AvatarResponse {
    @SerializedName("avatarUrl")
    private String avatarUrl;

    public String getAvatarUrl() {
        return avatarUrl;
    }
}