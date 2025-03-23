package com.example.cinema_booking_mobile.util;


import com.example.cinema_booking_mobile.service.IUserService;

public class ApiUtils {

    public static IUserService getUserService() {
        return ApiClient.getClient().create(IUserService.class);
    }
}
