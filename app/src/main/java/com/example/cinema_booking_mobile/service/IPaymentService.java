package com.example.cinema_booking_mobile.service;

import com.example.cinema_booking_mobile.dto.request.ThanhToanRequest;
import com.example.cinema_booking_mobile.dto.response.ChonGheResponse;
import com.example.cinema_booking_mobile.dto.response.PaymentMethodDTO;
import com.example.cinema_booking_mobile.dto.response.ThanhToanResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface IPaymentService {
    @POST("/api/dat-ve/chon-ghe")
    Call<ChonGheResponse> luuTrangThaiGiuGhe(@Header("Authorization") String token, @Body List<Integer> lichChieuGheId);

    @GET("/api/payment/get-enable-method")
    Call<List<PaymentMethodDTO>> getAllPhuongThucThanhToanByKhachHang(@Header("Authorization") String token);

    @POST("/api/dat-ve/thanh-toan")
    Call<ThanhToanResponse> thanhToan (@Header("Authorization") String token, @Body ThanhToanRequest request);

}
