package com.example.cinema_booking_mobile.model;

public class PaymentMethod {
    private int id;
    private String name;
    private String phoneNumber;
    private int logoResId;
    private String status;

    public PaymentMethod(int id, String name, String phoneNumber, int logoResId) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.logoResId = logoResId;
    }

    @Override
    public String toString() {
        return "PaymentMethod{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", logoResId=" + logoResId +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getLogoResId() {
        return logoResId;
    }

    public void setLogoResId(int logoResId) {
        this.logoResId = logoResId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}