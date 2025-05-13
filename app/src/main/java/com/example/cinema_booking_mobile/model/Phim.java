package com.example.cinema_booking_mobile.model;

public class Phim {
    private Integer id;
    private String ten;
    private String theLoai;
    private Integer doDai;
    private String ngonNgu;
    private String daoDien;
    private String dienVien;
    private String moTa;
    private String poster;
    private String trailer;
    private Integer namSx;
    private String hangSx;
    private String doTuoi;
    private Float danhGia = 0f;
    private String trangThai = "Sắp chiếu";

    public Phim() {}

    public Phim(
            Integer id, String ten, String theLoai, Integer doDai, String ngonNgu,
            String daoDien, String dienVien, String moTa, String poster, String trailer,
            Integer namSx, String hangSx, String doTuoi, Float danhGia, String trangThai) {
        this.id = id;
        this.ten = ten;
        this.theLoai = theLoai;
        this.doDai = doDai;
        this.ngonNgu = ngonNgu;
        this.daoDien = daoDien;
        this.dienVien = dienVien;
        this.moTa = moTa;
        this.poster = poster;
        this.trailer = trailer;
        this.namSx = namSx;
        this.hangSx = hangSx;
        this.doTuoi = doTuoi;
        this.danhGia = danhGia;
        this.trangThai = trangThai;
    }

    public Integer getId() { return id; }
    public String getTen() { return ten; }
    public String getTheLoai() { return theLoai; }
    public Integer getDoDai() { return doDai; }
    public String getNgonNgu() { return ngonNgu; }
    public String getDaoDien() { return daoDien; }
    public String getDienVien() { return dienVien; }
    public String getMoTa() { return moTa; }
    public String getPoster() { return poster; }
    public String getTrailer() { return trailer; }
    public Integer getNamSxt() { return namSx; }
    public String getHangSx() { return hangSx; }
    public String getDoTuoi() { return doTuoi; }
    public Float getDanhGia() { return danhGia; }
    public String getTrangThai() { return trangThai; }

    public void setId(Integer id) { this.id = id; }
    public void setTen(String ten) { this.ten = ten; }
    public void setTheLoai(String theLoai) { this.theLoai = theLoai; }
    public void setDoDai(Integer doDai) { this.doDai = doDai; }
    public void setNgonNgu(String ngonNgu) { this.ngonNgu = ngonNgu; }
    public void setDaoDien(String daoDien) { this.daoDien = daoDien; }
    public void setDienVien(String dienVien) { this.dienVien = dienVien; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    public void setPoster(String poster) { this.poster = poster; }
    public void setTrailer(String trailer) { this.trailer = trailer; }
    public void setNamSx(Integer namSx) { this.namSx = namSx; }
    public void setHangSx(String hangSx) { this.hangSx = hangSx; }
    public void setDoTuoi(String doTuoi) { this.doTuoi = doTuoi; }
    public void setDanhGia(Float danhGia) { this.danhGia = danhGia; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}
