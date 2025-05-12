package com.example.cinema_booking_mobile.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatter {
    private static final String API_DATE_FORMAT = "yyyy-MM-dd";
    private static final String DISPLAY_DATE_FORMAT = "dd/MM/yyyy";

    private static final String API_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String MM_DD_YYYY_FORMAT = "MM/dd/yyyy";


    public static String apiToDisplayFormat(String apiDate) {
        if (apiDate == null || apiDate.isEmpty()) {
            return "";
        }

        try {
            SimpleDateFormat apiFormat = new SimpleDateFormat(API_DATE_FORMAT, Locale.getDefault());
            SimpleDateFormat displayFormat = new SimpleDateFormat(DISPLAY_DATE_FORMAT, Locale.getDefault());
            Date date = apiFormat.parse(apiDate);
            return displayFormat.format(date);
        } catch (ParseException e) {
            return apiDate;
        }
    }

    public static String displayToApiFormat(String displayDate) {
        if (displayDate == null || displayDate.isEmpty()) {
            return "";
        }

        try {
            SimpleDateFormat displayFormat = new SimpleDateFormat(DISPLAY_DATE_FORMAT, Locale.getDefault());
            SimpleDateFormat apiFormat = new SimpleDateFormat(API_DATE_FORMAT, Locale.getDefault());
            Date date = displayFormat.parse(displayDate);
            return apiFormat.format(date);
        } catch (ParseException e) {
            return displayDate;
        }
    }

    public static String apiDateTimeToMMDDYYYY(String apiDateTime) {
        if (apiDateTime == null || apiDateTime.isEmpty()) {
            return "";
        }

        try {
            SimpleDateFormat apiFormat = new SimpleDateFormat(API_DATETIME_FORMAT, Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat(MM_DD_YYYY_FORMAT, Locale.getDefault());
            Date date = apiFormat.parse(apiDateTime);
            return outputFormat.format(date);
        } catch (ParseException e) {
            return apiDateTime;
        }
    }

}