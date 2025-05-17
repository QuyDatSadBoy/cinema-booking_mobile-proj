package com.example.cinema_booking_mobile.dto.response;

public class PaymentMethodDTO {
    private Integer id;
    private String ten;
    private String soTaiKhoan;
    private String status;

    public PaymentMethodDTO() {
    }

    public PaymentMethodDTO(Integer id, String soTaiKhoan, String ten) {
        this.id = id;
        this.soTaiKhoan = soTaiKhoan;
        this.ten = ten;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSoTaiKhoan() {
        return soTaiKhoan;
    }

    public void setSoTaiKhoan(String soTaiKhoan) {
        this.soTaiKhoan = soTaiKhoan;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}