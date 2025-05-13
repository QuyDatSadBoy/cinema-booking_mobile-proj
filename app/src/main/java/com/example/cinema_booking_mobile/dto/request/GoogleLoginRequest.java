package com.example.cinema_booking_mobile.dto.request;

public class GoogleLoginRequest {
    private String idToken;
    private String email;
    private String name;
    private String photoUrl;

    public GoogleLoginRequest(String idToken, String email, String name, String photoUrl) {
        this.idToken = idToken;
        this.email = email;
        this.name = name;
        this.photoUrl = photoUrl;
    }

    // Getters and setters
    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}