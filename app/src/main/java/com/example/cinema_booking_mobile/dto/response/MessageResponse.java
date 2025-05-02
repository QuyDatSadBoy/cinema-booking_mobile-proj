package com.example.cinema_booking_mobile.dto.response;

import com.google.gson.annotations.SerializedName;

public class MessageResponse {
    @SerializedName("message")
    private String message;

    @SerializedName("success")
    private boolean success;

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}