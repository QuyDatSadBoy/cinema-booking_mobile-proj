package com.example.cinema_booking_mobile.dto.response;

import java.io.Serializable;

public class GheDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer lichChieuGheId;
    private String ten;
    private String hang;
    private String cot;
    private String loaiGhe;
    private String trangThai;
    private boolean selected;

    public GheDTO(String cot, String hang, Integer id, Integer lichChieuGheId, String loaiGhe, boolean selected, String ten, String trangThai) {
        this.cot = cot;
        this.hang = hang;
        this.id = id;
        this.lichChieuGheId = lichChieuGheId;
        this.loaiGhe = loaiGhe;
        this.selected = selected;
        this.ten = ten;
        this.trangThai = trangThai;
    }

    public String getCot() {
        return cot;
    }

    public void setCot(String cot) {
        this.cot = cot;
    }

    public String getHang() {
        return hang;
    }

    public void setHang(String hang) {
        this.hang = hang;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLichChieuGheId() {
        return lichChieuGheId;
    }

    public void setLichChieuGheId(Integer lichChieuGheId) {
        this.lichChieuGheId = lichChieuGheId;
    }

    public String getLoaiGhe() {
        return loaiGhe;
    }

    public void setLoaiGhe(String loaiGhe) {
        this.loaiGhe = loaiGhe;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}

