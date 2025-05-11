package com.example.cinema_booking_mobile.model;

import com.google.gson.annotations.SerializedName;

public class UserProfile {
    @SerializedName("id")
    private Integer id;

    @SerializedName("email")
    private String email;

    @SerializedName("ten")
    private String name;

    @SerializedName("soDienThoai")
    private String phone;

    @SerializedName("ngaySinh")
    private String birthday;

    @SerializedName("gioiTinh")
    private String gender;

    @SerializedName("diaChi")
    private String address;

    @SerializedName("avatar")
    private String avatarUrl;

    @SerializedName("hangThanhVien")
    private String membershipTier;

    @SerializedName("diemTichLuy")
    private Integer loyaltyPoints;

    // Constructors
    public UserProfile() {
    }

    public UserProfile(Integer id, String email, String name, String phone, String birthday,
                       String gender, String address, String avatarUrl,
                       String membershipTier, Integer loyaltyPoints) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.birthday = birthday;
        this.gender = gender;
        this.address = address;
        this.avatarUrl = avatarUrl;
        this.membershipTier = membershipTier;
        this.loyaltyPoints = loyaltyPoints;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getMembershipTier() {
        return membershipTier;
    }

    public void setMembershipTier(String membershipTier) {
        this.membershipTier = membershipTier;
    }

    public Integer getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(Integer loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }
}