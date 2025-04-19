package com.example.cinema_booking_mobile.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "CinemaSession";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_REFRESH_TOKEN = "refreshToken";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_ROLE = "userRole";
    private static final String KEY_USER_AVATAR = "userAvatar";
    // Thêm các key mới cho thông tin cá nhân
    private static final String KEY_USER_PHONE = "userPhone";
    private static final String KEY_USER_BIRTHDAY = "userBirthday";
    private static final String KEY_USER_GENDER = "userGender";
    private static final String KEY_USER_ADDRESS = "userAddress";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveAuthToken(String token, String refreshToken) {
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_REFRESH_TOKEN, refreshToken);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    public void saveUserInfo(Integer userId, String email, String name, String role) {
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USER_NAME, name);
        editor.putString(KEY_USER_ROLE, role);
        editor.apply();
    }

    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    public String getRefreshToken() {
        return sharedPreferences.getString(KEY_REFRESH_TOKEN, null);
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public Integer getUserId() {
        return sharedPreferences.getInt(KEY_USER_ID, -1);
    }

    public String getUserEmail() {
        return sharedPreferences.getString(KEY_USER_EMAIL, null);
    }

    public String getUserName() {
        return sharedPreferences.getString(KEY_USER_NAME, null);
    }

    public String getUserRole() {
        return sharedPreferences.getString(KEY_USER_ROLE, null);
    }

    public String getAuthorizationHeader() {
        return "Bearer " + getToken();
    }

    public String getUserAvatar() {
        return sharedPreferences.getString(KEY_USER_AVATAR, null);
    }

    /**
     * Lưu avatar người dùng
     */
    public void setUserAvatar(String avatarUrl) {
        editor.putString(KEY_USER_AVATAR, avatarUrl);
        editor.apply();
    }

    /**
     * Lưu tên người dùng
     */
    public void setUserName(String name) {
        editor.putString(KEY_USER_NAME, name);
        editor.apply();
    }

    /**
     * Lưu email người dùng
     */
    public void setUserEmail(String email) {
        editor.putString(KEY_USER_EMAIL, email);
        editor.apply();
    }

    /**
     * Lưu số điện thoại người dùng
     */
    public void setUserPhone(String phone) {
        editor.putString(KEY_USER_PHONE, phone);
        editor.apply();
    }

    /**
     * Lưu ngày sinh người dùng
     */
    public void setUserBirthday(String birthday) {
        editor.putString(KEY_USER_BIRTHDAY, birthday);
        editor.apply();
    }

    /**
     * Lưu giới tính người dùng
     */
    public void setUserGender(String gender) {
        editor.putString(KEY_USER_GENDER, gender);
        editor.apply();
    }

    /**
     * Lưu địa chỉ người dùng
     */
    public void setUserAddress(String address) {
        editor.putString(KEY_USER_ADDRESS, address);
        editor.apply();
    }

    /**
     * Lấy số điện thoại người dùng
     */
    public String getUserPhone() {
        return sharedPreferences.getString(KEY_USER_PHONE, null);
    }

    /**
     * Lấy ngày sinh người dùng
     */
    public String getUserBirthday() {
        return sharedPreferences.getString(KEY_USER_BIRTHDAY, null);
    }

    /**
     * Lấy giới tính người dùng
     */
    public String getUserGender() {
        return sharedPreferences.getString(KEY_USER_GENDER, null);
    }

    /**
     * Lấy địa chỉ người dùng
     */
    public String getUserAddress() {
        return sharedPreferences.getString(KEY_USER_ADDRESS, null);
    }

    public void clearSession() {
        editor.clear();
        editor.apply();
    }
}