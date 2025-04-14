package com.example.cinema_booking_mobile.model;

import java.time.LocalDate;

public class DienVien {
    private Integer id;
    private String hoTen;
    private String gioiTinh;
    private LocalDate ngaySinh;
    private String quocTich;
    private String tieuSu;
    private String anhDaiDien;
    private LocalDate ngayTao;

    public DienVien(String hoTen, String gioiTinh, LocalDate ngaySinh, String quocTich, String tieuSu, String anhDaiDien, LocalDate ngayTao) {
        this.hoTen = hoTen;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.quocTich = quocTich;
        this.tieuSu = tieuSu;
        this.anhDaiDien = anhDaiDien;
        this.ngayTao = ngayTao;
    }

    public Integer getId() {
        return id;
    }

    public String getHoTen() {
        return hoTen;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public String getQuocTich() {
        return quocTich;
    }

    public String getTieuSu() {
        return tieuSu;
    }

    public String getAnhDaiDien() {
        return anhDaiDien;
    }

    public LocalDate getNgayTao() {
        return ngayTao;
    }

    // Setters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public void setQuocTich(String quocTich) {
        this.quocTich = quocTich;
    }

    public void setTieuSu(String tieuSu) {
        this.tieuSu = tieuSu;
    }

    public void setAnhDaiDien(String anhDaiDien) {
        this.anhDaiDien = anhDaiDien;
    }

    public void setNgayTao(LocalDate ngayTao) {
        this.ngayTao = ngayTao;
    }
}
