package com.example.cinema_booking_mobile.util;

import android.content.Context;
import android.content.SharedPreferences;

public class ChatbotPreferences {
    private static final String PREF_NAME = "ChatbotPreferences";
    private static final String KEY_CINEMA_NAME = "cinema_name";
    private static final String KEY_CINEMA_LOCATION = "cinema_location";
    private static final String KEY_CINEMA_DESCRIPTION = "cinema_description";
    private static final String KEY_TICKET_PRICES = "ticket_prices";
    private static final String KEY_OPENING_HOURS = "opening_hours";
    private static final String KEY_FACILITIES = "facilities";
    private static final String KEY_CURRENT_MOVIES = "current_movies";
    private static final String KEY_UPCOMING_MOVIES = "upcoming_movies";
    private static final String KEY_SPECIAL_PROMOTIONS = "special_promotions";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public ChatbotPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Cinema Name
    public void setCinemaName(String cinemaName) {
        editor.putString(KEY_CINEMA_NAME, cinemaName);
        editor.apply();
    }

    public String getCinemaName() {
        return sharedPreferences.getString(KEY_CINEMA_NAME, "Bài tập lớn Android Cô Vân Anh Cinema");
    }

    // Cinema Location
    public void setCinemaLocation(String location) {
        editor.putString(KEY_CINEMA_LOCATION, location);
        editor.apply();
    }

    public String getCinemaLocation() {
        return sharedPreferences.getString(KEY_CINEMA_LOCATION, "Học viện Công Nghệ Bưu Chính Viễn Thông, PTIT, Trần Phú, TP.Hà Nội");
    }

    // Cinema Description
    public void setCinemaDescription(String description) {
        editor.putString(KEY_CINEMA_DESCRIPTION, description);
        editor.apply();
    }

    public String getCinemaDescription() {
        return sharedPreferences.getString(KEY_CINEMA_DESCRIPTION,
                "Bài tập lớn Android Cô Vân Anh Cinema là một trong những chuỗi rạp chiếu phim hàng đầu Việt Nam, " +
                        "cung cấp trải nghiệm xem phim chất lượng cao với công nghệ âm thanh và hình ảnh hiện đại.");
    }

    // Ticket Prices
    public void setTicketPrices(String prices) {
        editor.putString(KEY_TICKET_PRICES, prices);
        editor.apply();
    }

    public String getTicketPrices() {
        return sharedPreferences.getString(KEY_TICKET_PRICES,
                "Phim 2D: 60.000đ - 90.000đ\n" +
                        "Phim 3D: 90.000đ - 120.000đ\n" +
                        "IMAX: 150.000đ - 180.000đ\n" +
                        "Ghế đôi: 200.000đ - 250.000đ");
    }

    // Opening Hours
    public void setOpeningHours(String hours) {
        editor.putString(KEY_OPENING_HOURS, hours);
        editor.apply();
    }

    public String getOpeningHours() {
        return sharedPreferences.getString(KEY_OPENING_HOURS,
                "Thứ 2 - Thứ 6: 10:00 - 22:00\n" +
                        "Thứ 7, Chủ nhật: 9:00 - 23:00\n" +
                        "Ngày lễ: 8:00 - 24:00");
    }

    // Facilities
    public void setFacilities(String facilities) {
        editor.putString(KEY_FACILITIES, facilities);
        editor.apply();
    }

    public String getFacilities() {
        return sharedPreferences.getString(KEY_FACILITIES,
                "Phòng chiếu 2D, 3D, IMAX\n" +
                        "Khu vực ẩm thực, popcorn và nước uống\n" +
                        "Khu vui chơi trẻ em\n" +
                        "Wifi miễn phí\n" +
                        "Bãi đậu xe rộng rãi");
    }

    // Current Movies
    public void setCurrentMovies(String movies) {
        editor.putString(KEY_CURRENT_MOVIES, movies);
        editor.apply();
    }

    public String getCurrentMovies() {
        return sharedPreferences.getString(KEY_CURRENT_MOVIES,
                "Cuốn Theo Chiều Gió - Lang mạn - 120 phút\n" +
                        "Vũ Trụ Bao La - Khoa học viễn tưởng - 140 phút\n" +
                        "Ám Ảnh Tối Tối - Kinh dị - 95 phút\n" +
                        "Vũ Điệu Đêm Hè - Âm nhạc - 100 phút");
    }

    // Upcoming Movies
    public void setUpcomingMovies(String movies) {
        editor.putString(KEY_UPCOMING_MOVIES, movies);
        editor.apply();
    }

    public String getUpcomingMovies() {
        return sharedPreferences.getString(KEY_UPCOMING_MOVIES,
                "Hành Trình Khám Phá - Phiêu lưu - 110 phút\n" +
                        "Ngày Trở Về - Tâm lý - 125 phút\n" +
                        "Kỷ Ức Tuổi Thơ - Gia đình - 90 phút");
    }

    // Special Promotions
    public void setSpecialPromotions(String promotions) {
        editor.putString(KEY_SPECIAL_PROMOTIONS, promotions);
        editor.apply();
    }

    public String getSpecialPromotions() {
        return sharedPreferences.getString(KEY_SPECIAL_PROMOTIONS,
                "Thứ 3 vui vẻ: Giảm 30% giá vé cho tất cả các suất chiếu vào thứ 3\n" +
                        "Happy Hour: Giảm 20% giá vé cho các suất chiếu trước 12:00 trưa\n" +
                        "Combo đôi: Mua 2 vé kèm bắp nước với giá ưu đãi");
    }

    // Clear all preferences
    public void clearAllPreferences() {
        editor.clear();
        editor.apply();
    }
}