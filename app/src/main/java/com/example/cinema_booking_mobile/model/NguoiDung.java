package com.example.cinema_booking_mobile.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class NguoiDung {
    private Integer id;
    private String email;
    private String ten;
    private String matKhau;
    private String soDienThoai;
    private LocalDate ngaySinh;
    private String gioiTinh;
    private String diaChi;
    private String avatar;
    private LocalDateTime thoiGianTao;
    private String trangThai;

    public NguoiDung(String avatar, String diaChi, String email, String gioiTinh, Integer id, String matKhau, LocalDate ngaySinh, String soDienThoai, String ten, LocalDateTime thoiGianTao, String trangThai) {
        this.avatar = avatar;
        this.diaChi = diaChi;
        this.email = email;
        this.gioiTinh = gioiTinh;
        this.id = id;
        this.matKhau = matKhau;
        this.ngaySinh = ngaySinh;
        this.soDienThoai = soDienThoai;
        this.ten = ten;
        this.thoiGianTao = thoiGianTao;
        this.trangThai = trangThai;
    }

    public NguoiDung() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public LocalDateTime getThoiGianTao() {
        return thoiGianTao;
    }

    public void setThoiGianTao(LocalDateTime thoiGianTao) {
        this.thoiGianTao = thoiGianTao;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}