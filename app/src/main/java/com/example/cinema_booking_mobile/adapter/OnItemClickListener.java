package com.example.cinema_booking_mobile.adapter;

import com.example.cinema_booking_mobile.model.DateItem;
import com.example.cinema_booking_mobile.model.TimeItem;

public interface OnItemClickListener {
    interface DateItemClickListener {
        void onDateItemClick(DateItem dateItem, int position);
    }

    interface TimeItemClickListener {
        void onTimeItemClick(TimeItem timeItem, int position);
    }

    interface PhimDangChieuListener {
        void onPhimDangChieuClick(int position);
    }

    interface PhimSapChieuListener {
        void onPhimSapChieuClick(int position);
    }

    interface PhimTimKiemListener {
        void onPhimTimKiemClick(int position);
    }
}