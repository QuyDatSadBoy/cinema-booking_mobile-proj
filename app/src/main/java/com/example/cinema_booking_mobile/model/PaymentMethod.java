package com.example.cinema_booking_mobile.model;

public class PaymentMethod {
    private int id;
    private String name;
    private String phoneNumber;
    private int logoResId;

    public PaymentMethod(int id, String name, String phoneNumber, int logoResId) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.logoResId = logoResId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getLogoResId() {
        return logoResId;
    }
}