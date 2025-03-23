package com.example.cinema_booking_mobile.model;

import java.time.LocalDateTime;

public class KhachHang extends NguoiDung {
    private String maThanhVien;
    private Integer diemTichLuy;
    private String hangThanhVien;
    private LocalDateTime ngayDangKy;

    public KhachHang() {
        super();
    }

    public KhachHang(String maThanhVien, Integer diemTichLuy, String hangThanhVien, LocalDateTime ngayDangKy) {
        super();
        this.maThanhVien = maThanhVien;
        this.diemTichLuy = diemTichLuy;
        this.hangThanhVien = hangThanhVien;
        this.ngayDangKy = ngayDangKy;
    }

    // Getters and Setters
    public String getMaThanhVien() {
        return maThanhVien;
    }

    public void setMaThanhVien(String maThanhVien) {
        this.maThanhVien = maThanhVien;
    }

    public Integer getDiemTichLuy() {
        return diemTichLuy;
    }

    public void setDiemTichLuy(Integer diemTichLuy) {
        this.diemTichLuy = diemTichLuy;
    }

    public String getHangThanhVien() {
        return hangThanhVien;
    }

    public void setHangThanhVien(String hangThanhVien) {
        this.hangThanhVien = hangThanhVien;
    }

    public LocalDateTime getNgayDangKy() {
        return ngayDangKy;
    }

    public void setNgayDangKy(LocalDateTime ngayDangKy) {
        this.ngayDangKy = ngayDangKy;
    }
}