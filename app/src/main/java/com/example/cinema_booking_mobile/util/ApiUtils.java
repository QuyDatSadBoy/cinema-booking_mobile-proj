package com.example.cinema_booking_mobile.util;


import com.example.cinema_booking_mobile.service.IAuthService;
import com.example.cinema_booking_mobile.service.IUserService;

public class ApiUtils {

    public static IUserService getUserService() {
        return ApiClient.getClient().create(IUserService.class);
    }

    public static IAuthService getAuthService() {
        return ApiClient.getClient().create(IAuthService.class);
    }
}
