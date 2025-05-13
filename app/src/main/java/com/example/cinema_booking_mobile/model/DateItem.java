package com.example.cinema_booking_mobile.model;

import java.io.Serializable;
import java.time.LocalDate;

public class DateItem implements Serializable {
    private static final long serialVersionUID = 1L;
    private String dayName;
    private String dayNumber;
    private boolean isAvailable;
    private boolean isSelected;
    private LocalDate date;

    public DateItem(String dayName, String dayNumber, LocalDate date) {
        this.dayName = dayName;
        this.dayNumber = dayNumber;
        this.isAvailable = true;
        this.isSelected = false;
        this.date = date;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}