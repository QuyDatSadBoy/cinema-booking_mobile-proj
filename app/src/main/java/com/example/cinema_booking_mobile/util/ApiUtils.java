package com.example.cinema_booking_mobile.util;


import com.example.cinema_booking_mobile.service.IAuthService;
import com.example.cinema_booking_mobile.service.IPerplexityService;
import com.example.cinema_booking_mobile.service.IProfileService;
import com.example.cinema_booking_mobile.service.IUserService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiUtils {
    private static final String BASE_URL = "http://10.0.2.2:8080/api/";
    private static final String PERPLEXITY_API_URL = "https://api.perplexity.ai/";
    private static Retrofit perplexityRetrofit = null;

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

    public static IPerplexityService getPerplexityService() {
        if (perplexityRetrofit == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            perplexityRetrofit = new Retrofit.Builder()
                    .baseUrl(PERPLEXITY_API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }

        return perplexityRetrofit.create(IPerplexityService.class);
    }
}
