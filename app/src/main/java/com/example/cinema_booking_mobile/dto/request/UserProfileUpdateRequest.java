package com.example.cinema_booking_mobile.dto.request;

import com.google.gson.annotations.SerializedName;

public class UserProfileUpdateRequest {
    @SerializedName("ten")
    private String ten;

    @SerializedName("soDienThoai")
    private String soDienThoai;

    @SerializedName("ngaySinh")
    private String ngaySinh;

    @SerializedName("gioiTinh")
    private String gioiTinh;

    @SerializedName("diaChi")
    private String diaChi;

    // Constructors
    public UserProfileUpdateRequest() {
    }

    public UserProfileUpdateRequest(String ten, String soDienThoai, String ngaySinh, String gioiTinh, String diaChi) {
        this.ten = ten;
        this.soDienThoai = soDienThoai;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.diaChi = diaChi;
    }

    // Getters and Setters
    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }
}