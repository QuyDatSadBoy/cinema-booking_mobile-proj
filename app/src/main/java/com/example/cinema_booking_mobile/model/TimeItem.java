package com.example.cinema_booking_mobile.model;

public class TimeItem {
    private String time;
    private boolean isAvailable;
    private boolean isSelected;

    public TimeItem(String time) {
        this.time = time;
        this.isAvailable = true;
        this.isSelected = false;
    }

    public TimeItem(String time, boolean isAvailable, boolean isSelected) {
        this.time = time;
        this.isAvailable = isAvailable;
        this.isSelected = isSelected;
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
}