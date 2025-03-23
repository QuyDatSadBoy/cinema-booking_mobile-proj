package com.example.cinema_booking_mobile.dto.request;

public class LoginRequest {
    private String email;
    private String password;

    public LoginRequest( String email,String password) {
        this.password = password;
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String username) {
        this.email = username;
    }
}