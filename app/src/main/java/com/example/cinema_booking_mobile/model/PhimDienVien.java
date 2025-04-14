package com.example.cinema_booking_mobile.model;

import com.example.cinema_booking_mobile.model.DienVien;
import com.example.cinema_booking_mobile.model.Phim;

public class PhimDienVien {
    private Integer id;
    private Phim phim;
    private DienVien dienVien;
    private String vaiDien;
    private Boolean laChinh = false;
    private String ghiChu;

    public PhimDienVien(Phim phim, DienVien dienVien, String vaiDien, Boolean laChinh, String ghiChu) {
        this.phim = phim;
        this.dienVien = dienVien;
        this.vaiDien = vaiDien;
        this.laChinh = laChinh;
        this.ghiChu = ghiChu;
    }

    public Integer getId() {
        return id;
    }

    public Phim getPhim() {
        return phim;
    }

    public DienVien getDienVien() {
        return dienVien;
    }

    public String getVaiDien() {
        return vaiDien;
    }

    public Boolean getLaChinh() {
        return laChinh;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPhim(Phim phim) {
        this.phim = phim;
    }

    public void setDienVien(DienVien dienVien) {
        this.dienVien = dienVien;
    }

    public void setVaiDien(String vaiDien) {
        this.vaiDien = vaiDien;
    }

    public void setLaChinh(Boolean laChinh) {
        this.laChinh = laChinh;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}
