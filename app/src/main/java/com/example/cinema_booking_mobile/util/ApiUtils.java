package com.example.cinema_booking_mobile.util;


import com.example.cinema_booking_mobile.service.IAuthService;
import com.example.cinema_booking_mobile.service.ILichChieuService;
import com.example.cinema_booking_mobile.service.IPaymentService;
import com.example.cinema_booking_mobile.service.IPhimService;
import com.example.cinema_booking_mobile.service.IProfileService;
import com.example.cinema_booking_mobile.service.ITicketHistoryService;
import com.example.cinema_booking_mobile.service.IUserService;

public class ApiUtils {

    public static IUserService getUserService() {
        return ApiClient.getClient().create(IUserService.class);
    }

    public static IAuthService getAuthService() {
        return ApiClient.getClient().create(IAuthService.class);
    }

    // Thêm phương thức mới
    public static IProfileService getProfileService() {
        return ApiClient.getClient().create(IProfileService.class);
    }

    public static ILichChieuService getLichChieuService(){
        return ApiClient.getClient().create(ILichChieuService.class);
    }

    public static IPaymentService getPaymentService(){
        return ApiClient.getClient().create(IPaymentService.class);
    }

    public static ITicketHistoryService getTicketHistoryService() {
        return ApiClient.getClient().create(ITicketHistoryService.class);
    }

    public static IPhimService getPhimService(){
        return ApiClient.getClient().create(IPhimService.class);
    }
}
