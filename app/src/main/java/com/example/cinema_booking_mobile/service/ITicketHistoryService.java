package com.example.cinema_booking_mobile.service;

import com.example.cinema_booking_mobile.model.Ticket;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ITicketHistoryService {
    /**
     * Lấy tất cả lịch sử đặt vé của người dùng
     */
    @GET("/api/ticket/history/all")
    Call<List<Ticket>> getAllTickets(@Header("Authorization") String token);

}
