package com.example.cinema_booking_mobile.dto.response;

import java.util.List;

public class GioChieuReponse {
    private boolean  success;
    private String message;
    private List<LichChieuDTO> data;

    public GioChieuReponse() {}

    public GioChieuReponse(List<LichChieuDTO> data, String message, boolean success) {
        this.data = data;
        this.message = message;
        this.success = success;
    }

    public List<LichChieuDTO> getData() {
        return data;
    }

    public void setData(List<LichChieuDTO> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
