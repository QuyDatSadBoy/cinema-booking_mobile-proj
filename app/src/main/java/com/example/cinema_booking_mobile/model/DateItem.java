package com.example.cinema_booking_mobile.model;

public class DateItem {
    private String dayName;
    private String dayNumber;
    private boolean isAvailable;
    private boolean isSelected;

    public DateItem(String dayName, String dayNumber) {
        this.dayName = dayName;
        this.dayNumber = dayNumber;
        this.isAvailable = true;
        this.isSelected = false;
    }

    public DateItem(String dayName, String dayNumber, boolean isAvailable, boolean isSelected) {
        this.dayName = dayName;
        this.dayNumber = dayNumber;
        this.isAvailable = isAvailable;
        this.isSelected = isSelected;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(String dayNumber) {
        this.dayNumber = dayNumber;
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