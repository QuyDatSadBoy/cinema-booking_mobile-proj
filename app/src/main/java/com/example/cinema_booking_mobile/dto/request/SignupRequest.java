package com.example.cinema_booking_mobile.dto.request;

import java.util.Date;

public class SignupRequest {
    private String email;
    private String ten;
    private String matKhau;
    private String soDienThoai;
    private String ngaySinh; // Format: yyyy-MM-dd
    private String gioiTinh;
    private String diaChi;

    public SignupRequest(String email, String ten, String matKhau) {
        this.email = email;
        this.ten = ten;
        this.matKhau = matKhau;
    }

    public SignupRequest(String email, String ten, String matKhau, String soDienThoai,
                         String ngaySinh, String gioiTinh, String diaChi) {
        this.email = email;
        this.ten = ten;
        this.matKhau = matKhau;
        this.soDienThoai = soDienThoai;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.diaChi = diaChi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
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