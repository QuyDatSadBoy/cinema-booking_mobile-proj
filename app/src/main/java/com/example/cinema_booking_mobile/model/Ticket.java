package com.example.cinema_booking_mobile.model;

import com.google.gson.annotations.SerializedName;

public class Ticket {
    @SerializedName("id")
    private int id;

    @SerializedName("maVe")
    private String ticketCode;

    @SerializedName("tenPhim")
    private String movieTitle;

    @SerializedName("tenPhong")
    private String cinema;

    @SerializedName("ngayTao")
    private String date;

    @SerializedName("trangThai")
    private String status;

    @SerializedName("tenGhe")
    private String seatNumbers;
    @SerializedName("poster")
    private String posterUrl;
    public Ticket(int id, String ticketCode, String movieTitle, String cinema, String date, String seatNumbers, String status, String posterUrl) {
        this.id = id;
        this.ticketCode = ticketCode;
        this.movieTitle = movieTitle;
        this.cinema = cinema;
        this.date = date;
        this.seatNumbers = seatNumbers;
        this.status = status;
        this.posterUrl = posterUrl;
    }

    public Ticket() {
    }

    // Getters và setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getCinema() {
        return cinema;
    }

    public void setCinema(String cinema) {
        this.cinema = cinema;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSeatNumbers() {
        return seatNumbers;
    }

    public void setSeatNumbers(String seatNumbers) {
        this.seatNumbers = seatNumbers;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }
}