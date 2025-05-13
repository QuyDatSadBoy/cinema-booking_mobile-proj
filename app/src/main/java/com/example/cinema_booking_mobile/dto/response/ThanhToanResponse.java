package com.example.cinema_booking_mobile.dto.response;

import java.util.List;

public class ThanhToanResponse {
    private String maQR;
    private String maGiaoDich;
    private List<String> gheDaDat;
    private Double tongTien;
    private List<String> maCacVe;

    public ThanhToanResponse(List<String> gheDaDat, List<String> maCacVe, String maGiaoDich, String maQR, Double tongTien) {
        this.gheDaDat = gheDaDat;
        this.maCacVe = maCacVe;
        this.maGiaoDich = maGiaoDich;
        this.maQR = maQR;
        this.tongTien = tongTien;
    }

    public List<String> getGheDaDat() {
        return gheDaDat;
    }

    public void setGheDaDat(List<String> gheDaDat) {
        this.gheDaDat = gheDaDat;
    }

    public List<String> getMaCacVe() {
        return maCacVe;
    }

    public void setMaCacVe(List<String> maCacVe) {
        this.maCacVe = maCacVe;
    }

    public String getMaGiaoDich() {
        return maGiaoDich;
    }

    public void setMaGiaoDich(String maGiaoDich) {
        this.maGiaoDich = maGiaoDich;
    }

    public String getMaQR() {
        return maQR;
    }

    public void setMaQR(String maQR) {
        this.maQR = maQR;
    }

    public Double getTongTien() {
        return tongTien;
    }

    public void setTongTien(Double tongTien) {
        this.tongTien = tongTien;
    }
}