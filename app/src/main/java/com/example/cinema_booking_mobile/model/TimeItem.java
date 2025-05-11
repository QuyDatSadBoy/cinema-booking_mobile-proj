package com.example.cinema_booking_mobile.model;

import java.io.Serializable;

public class TimeItem implements Serializable {
    private static final long serialVersionUID = 1L;
    private String time;
    private boolean isAvailable;
    private boolean isSelected;
    private int lichChieuId;

    public TimeItem(String time, int lichChieuId) {
        this.time = time;
        this.isAvailable = true;
        this.isSelected = false;
        this.lichChieuId = lichChieuId;
    }

    public TimeItem(String time, boolean isAvailable, boolean isSelected, int lichChieuId) {
        this.time = time;
        this.isAvailable = isAvailable;
        this.isSelected = isSelected;
        this.lichChieuId = lichChieuId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getLichChieuId() {
        return lichChieuId;
    }

    public void setLichChieuId(int lichChieuId) {
        this.lichChieuId = lichChieuId;
    }
}