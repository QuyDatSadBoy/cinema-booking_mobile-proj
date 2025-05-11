package com.example.cinema_booking_mobile.service;

import com.example.cinema_booking_mobile.dto.response.GioChieuReponse;
import com.example.cinema_booking_mobile.dto.response.SoDoGheResponse;

import java.time.LocalDate;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ILichChieuService {
    @GET("/api/lich-chieu/gio-chieu/{phimId}")
    Call<GioChieuReponse> getGioChieuByPhimIdAndNgayAndPhongId(@Path("phimId") Integer id, @Query("ngay") LocalDate  ngay);

    @GET("/api/lich-chieu/so-do-ghe/{lichChieuId}")
    Call<SoDoGheResponse> getSoDoGheByLichChieuId(
            @Path("lichChieuId") Integer lichChieuId
    );

}
