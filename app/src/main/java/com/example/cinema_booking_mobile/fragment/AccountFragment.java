package com.example.cinema_booking_mobile.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cinema_booking_mobile.R;
import com.example.cinema_booking_mobile.activity.LoginActivity;
import com.example.cinema_booking_mobile.activity.PersonaInfoActivity;
import com.example.cinema_booking_mobile.model.UserProfile;
import com.example.cinema_booking_mobile.util.ApiUtils;
import com.example.cinema_booking_mobile.util.NetworkUtil;
import com.example.cinema_booking_mobile.util.SessionManager;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragment extends Fragment {
    private static final String TAG = "AccountFragment";
    // Shared Preferences Constants
    private static final String PREF_USER_INFO = "UserInfoPreferences";
    private static final String KEY_USER_AVATAR = "userAvatar";

    private ImageView ivAvatar;
    private TextView tvName, tvEmail;
    private View layoutPersonalInfo, layoutBookingHistory, layoutPaymentMethods, layoutFavoriteMovies, layoutLogout;
    private SessionManager sessionManager;
    private SharedPreferences userInfoPreferences;
    private UserProfile userProfile;

    // Thêm constant
    private static final int REQUEST_CODE_PERSONAL_INFO = 1001;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        // Khởi tạo
        sessionManager = new SessionManager(requireContext());
        userInfoPreferences = requireContext().getSharedPreferences(PREF_USER_INFO, Context.MODE_PRIVATE);
        userProfile = new UserProfile();

        // Ánh xạ các view
        ivAvatar = view.findViewById(R.id.ivAvatar);
        tvName = view.findViewById(R.id.tvName);
        tvEmail = view.findViewById(R.id.tvEmail);
        layoutPersonalInfo = view.findViewById(R.id.layoutPersonalInfo);
        layoutBookingHistory = view.findViewById(R.id.layoutBookingHistory);
        layoutPaymentMethods = view.findViewById(R.id.layoutPaymentMethods);
        layoutFavoriteMovies = view.findViewById(R.id.layoutFavoriteMovies);
        layoutLogout = view.findViewById(R.id.layoutLogout);

        // Lấy thông tin người dùng
        loadUserProfileFromServer();

        // Thiết lập sự kiện click
        setupClickListeners();

        return view;
    }


    private void loadUserProfile() {
        // Trong tương lai, đây sẽ là nơi để gọi API lấy thông tin người dùng
        // Ví dụ:
        // ApiClient.getApiService().getUserProfile(userId).enqueue(new Callback<UserProfile>() { ... });

        // Hiện tại, lấy thông tin cơ bản từ SessionManager (thông tin đăng nhập)
        userProfile.setId(sessionManager.getUserId());
        userProfile.setName(sessionManager.getUserName());
        userProfile.setEmail(sessionManager.getUserEmail());

        // Lấy thông tin avatar từ SharedPreferences
        userProfile.setAvatarUrl(userInfoPreferences.getString(KEY_USER_AVATAR, null));

        // Hiển thị dữ liệu lên giao diện
        displayUserInfo();
    }

    private void displayUserInfo() {
        // Hiển thị tên và email
        if (userProfile.getName() != null) {
            tvName.setText(userProfile.getName());
        }

        if (userProfile.getEmail() != null) {
            tvEmail.setText(userProfile.getEmail());
        }

        // Nếu có avatar thì hiển thị
        if (userProfile.getAvatarUrl() != null && !userProfile.getAvatarUrl().isEmpty()) {
            String avatarUrl = userProfile.getAvatarUrl();

            // Kiểm tra xem URL có phải là URL đầy đủ hay không
            if (!avatarUrl.startsWith("http")) {
                // Nếu là URL tương đối, ghép với base URL
                String baseUrl = "http://10.0.2.2:8080/api";
                avatarUrl = baseUrl + avatarUrl;
            }

            Picasso.get()
                    .load(avatarUrl)
                    .placeholder(R.drawable.ic_person)
                    .error(R.drawable.ic_person)
                    .into(ivAvatar);
        } else {
            ivAvatar.setImageResource(R.drawable.ic_person);
        }
    }

    private void setupClickListeners() {
        // Xử lý sự kiện khi click vào thông tin cá nhân
        layoutPersonalInfo.setOnClickListener(v -> {
            // Mở màn hình thông tin cá nhân
            Intent intent = new Intent(requireContext(), PersonaInfoActivity.class);
            startActivity(intent);
        });

        // Xử lý sự kiện khi click vào lịch sử đặt vé
        layoutBookingHistory.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Chức năng đang được phát triển", Toast.LENGTH_SHORT).show();
        });

        // Xử lý sự kiện khi click vào phương thức thanh toán
        layoutPaymentMethods.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Chức năng đang được phát triển", Toast.LENGTH_SHORT).show();
        });

        // Xử lý sự kiện khi click vào phim yêu thích
        layoutFavoriteMovies.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Chức năng đang được phát triển", Toast.LENGTH_SHORT).show();
        });

        // Xử lý sự kiện khi click vào đăng xuất
        layoutLogout.setOnClickListener(v -> {
            logoutUser();
        });
    }

    private void logoutUser() {
        // Trong tương lai, đây sẽ là nơi để gọi API đăng xuất
        // Ví dụ:
        // ApiClient.getApiService().logout().enqueue(new Callback<Response>() { ... });

        // Xóa thông tin đăng nhập từ SessionManager
        sessionManager.clearSession();

        // Xóa thông tin người dùng từ SharedPreferences
        SharedPreferences.Editor editor = userInfoPreferences.edit();
        editor.clear();
        editor.apply();

        // Xác nhận đăng xuất
        Toast.makeText(requireContext(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show();

        // Chuyển về màn hình đăng nhập
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }

    private void loadUserProfileFromServer() {
        // Kiểm tra kết nối internet
        if (NetworkUtil.isNetworkAvailable(requireContext())) {
            String token = sessionManager.getAuthorizationHeader();

            // Gọi API để lấy thông tin profile đầy đủ
            ApiUtils.getProfileService().getUserProfile(token).enqueue(new Callback<UserProfile>() {
                @Override
                public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        userProfile = response.body();
                        displayUserInfo();

                        // Lưu vào cache
                        saveUserInfoToCache();
                    } else {
                        // Nếu không lấy được từ server, load từ cache
                        loadUserProfile();
                    }
                }

                @Override
                public void onFailure(Call<UserProfile> call, Throwable t) {
                    // Nếu lỗi, load từ cache
                    loadUserProfile();
                }
            });
        } else {
            // Không có internet, load từ cache
            loadUserProfile();
        }
    }

    private void saveUserInfoToCache() {
        SharedPreferences.Editor editor = userInfoPreferences.edit();
        if (userProfile.getAvatarUrl() != null) {
            editor.putString(KEY_USER_AVATAR, userProfile.getAvatarUrl());
        }
        editor.apply();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Load lại thông tin từ server khi quay lại fragment
        loadUserProfileFromServer();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PERSONAL_INFO && resultCode == Activity.RESULT_OK) {
            // Refresh data khi quay lại từ PersonalInfoActivity
            loadUserProfileFromServer();
        }
    }

}