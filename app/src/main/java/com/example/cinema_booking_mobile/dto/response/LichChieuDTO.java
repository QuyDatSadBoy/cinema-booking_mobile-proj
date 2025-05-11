package com.example.cinema_booking_mobile.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

public class LichChieuDTO {
    private Integer id;
    private Date batDau;
    private Date  ketThuc;
    private BigDecimal giaVe;
    private String tenPhong;
    private String loaiPhong;

    public LichChieuDTO() {}

    public LichChieuDTO(Date batDau, BigDecimal giaVe, Integer id, Date ketThuc, String loaiPhong, String tenPhong) {
        this.batDau = batDau;
        this.giaVe = giaVe;
        this.id = id;
        this.ketThuc = ketThuc;
        this.loaiPhong = loaiPhong;
        this.tenPhong = tenPhong;
    }

    public Date getBatDau() {
        return batDau;
    }

    public void setBatDau(Date batDau) {
        this.batDau = batDau;
    }

    public BigDecimal getGiaVe() {
        return giaVe;
    }

    public void setGiaVe(BigDecimal giaVe) {
        this.giaVe = giaVe;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getKetThuc() {
        return ketThuc;
    }

    public void setKetThuc(Date ketThuc) {
        this.ketThuc = ketThuc;
    }

    public String getLoaiPhong() {
        return loaiPhong;
    }

    public void setLoaiPhong(String loaiPhong) {
        this.loaiPhong = loaiPhong;
    }

    public String getTenPhong() {
        return tenPhong;
    }

    public void setTenPhong(String tenPhong) {
        this.tenPhong = tenPhong;
    }
}

