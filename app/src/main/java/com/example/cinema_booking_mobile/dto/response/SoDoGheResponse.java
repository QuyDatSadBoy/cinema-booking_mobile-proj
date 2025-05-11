package com.example.cinema_booking_mobile.dto.response;

import java.util.List;
import java.util.Map;

public class SoDoGheResponse {
    private Integer lichChieuId;
    private String tenPhim;
    private String batDau;
    private String ketThuc;
    private String tenPhong;
    private String loaiPhong;
    private Double giaVe;
    private Map<String, List<GheDTO>> soDoGhe;

    public SoDoGheResponse(String batDau, Double giaVe, String ketThuc, Integer lichChieuId, String loaiPhong, Map<String, List<GheDTO>> soDoGhe, String tenPhim, String tenPhong) {
        this.batDau = batDau;
        this.giaVe = giaVe;
        this.ketThuc = ketThuc;
        this.lichChieuId = lichChieuId;
        this.loaiPhong = loaiPhong;
        this.soDoGhe = soDoGhe;
        this.tenPhim = tenPhim;
        this.tenPhong = tenPhong;
    }

    public String getBatDau() {
        return batDau;
    }

    public void setBatDau(String batDau) {
        this.batDau = batDau;
    }

    public Double getGiaVe() {
        return giaVe;
    }

    public void setGiaVe(Double giaVe) {
        this.giaVe = giaVe;
    }

    public String getKetThuc() {
        return ketThuc;
    }

    public void setKetThuc(String ketThuc) {
        this.ketThuc = ketThuc;
    }

    public Integer getLichChieuId() {
        return lichChieuId;
    }

    public void setLichChieuId(Integer lichChieuId) {
        this.lichChieuId = lichChieuId;
    }

    public String getLoaiPhong() {
        return loaiPhong;
    }

    public void setLoaiPhong(String loaiPhong) {
        this.loaiPhong = loaiPhong;
    }

    public Map<String, List<GheDTO>> getSoDoGhe() {
        return soDoGhe;
    }

    public void setSoDoGhe(Map<String, List<GheDTO>> soDoGhe) {
        this.soDoGhe = soDoGhe;
    }

    public String getTenPhim() {
        return tenPhim;
    }

    public void setTenPhim(String tenPhim) {
        this.tenPhim = tenPhim;
    }

    public String getTenPhong() {
        return tenPhong;
    }

    public void setTenPhong(String tenPhong) {
        this.tenPhong = tenPhong;
    }
}