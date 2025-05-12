package com.example.cinema_booking_mobile.service;

import com.example.cinema_booking_mobile.dto.response.PhimDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IPhimService {
    @GET("/api/phim")
    Call<List<PhimDTO>> getAllPhim();

    @GET("/api/phim/{maPhim}")
    Call<PhimDTO> getPhim(@Path("maPhim") Integer maPhim);

}
