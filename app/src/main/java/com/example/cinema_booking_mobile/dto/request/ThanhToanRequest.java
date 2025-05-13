package com.example.cinema_booking_mobile.dto.request;

import java.util.List;

public class ThanhToanRequest {
    private List<Integer> lichChieuGheIds;
    private Double tongTien;
    private Integer phuongThucThanhToanId;

    public ThanhToanRequest(List<Integer> lichChieuGheIds, Integer phuongThucThanhToanId, Double tongTien) {
        this.lichChieuGheIds = lichChieuGheIds;
        this.phuongThucThanhToanId = phuongThucThanhToanId;
        this.tongTien = tongTien;
    }

    public List<Integer> getLichChieuGheIds() {
        return lichChieuGheIds;
    }

    public void setLichChieuGheIds(List<Integer> lichChieuGheIds) {
        this.lichChieuGheIds = lichChieuGheIds;
    }

    public Integer getPhuongThucThanhToanId() {
        return phuongThucThanhToanId;
    }

    public void setPhuongThucThanhToanId(Integer phuongThucThanhToanId) {
        this.phuongThucThanhToanId = phuongThucThanhToanId;
    }

    public Double getTongTien() {
        return tongTien;
    }

    public void setTongTien(Double tongTien) {
        this.tongTien = tongTien;
    }
}