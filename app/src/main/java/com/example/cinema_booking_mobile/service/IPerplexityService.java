package com.example.cinema_booking_mobile.service;

import com.example.cinema_booking_mobile.service.perplexity.PerplexityRequest;
import com.example.cinema_booking_mobile.service.perplexity.PerplexityResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface IPerplexityService {
    @POST("chat/completions")
    Call<PerplexityResponse> getCompletion(
            @Header("Authorization") String authorization,
            @Body PerplexityRequest request
    );

    // Overloaded method that automatically adds the API key
    default Call<PerplexityResponse> getCompletion(PerplexityRequest request) {
        return getCompletion("Bearer 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", request);
    }
}