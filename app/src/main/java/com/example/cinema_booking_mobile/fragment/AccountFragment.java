package com.example.cinema_booking_mobile.fragment;

import android.content.Intent;
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
import com.example.cinema_booking_mobile.activity.PersonalInfoActivity;
import com.example.cinema_booking_mobile.util.SessionManager;
import com.squareup.picasso.Picasso;

public class AccountFragment extends Fragment {

    private ImageView ivAvatar;
    private TextView tvName, tvEmail;
    private View layoutPersonalInfo, layoutBookingHistory, layoutPaymentMethods, layoutFavoriteMovies, layoutLogout;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        // Khởi tạo SessionManager
        sessionManager = new SessionManager(requireContext());

        // Ánh xạ các view
        ivAvatar = view.findViewById(R.id.ivAvatar);
        tvName = view.findViewById(R.id.tvName);
        tvEmail = view.findViewById(R.id.tvEmail);
        layoutPersonalInfo = view.findViewById(R.id.layoutPersonalInfo);
        layoutBookingHistory = view.findViewById(R.id.layoutBookingHistory);
        layoutPaymentMethods = view.findViewById(R.id.layoutPaymentMethods);
        layoutFavoriteMovies = view.findViewById(R.id.layoutFavoriteMovies);
        layoutLogout = view.findViewById(R.id.layoutLogout);

        // Hiển thị thông tin người dùng
        displayUserInfo();

        // Thiết lập sự kiện click
        setupClickListeners();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Cập nhật thông tin người dùng khi quay lại fragment
        displayUserInfo();
    }

    private void displayUserInfo() {
        // Hiển thị tên và email từ thông tin đã lưu trong SessionManager
        String userName = sessionManager.getUserName();
        String userEmail = sessionManager.getUserEmail();

        if (userName != null) {
            tvName.setText(userName);
        }

        if (userEmail != null) {
            tvEmail.setText(userEmail);
        }

        // Nếu có avatar thì hiển thị
        String avatar = sessionManager.getUserAvatar();
        if (avatar != null && !avatar.isEmpty()) {
            Picasso.get().load(avatar).placeholder(R.drawable.ic_person).into(ivAvatar);
        }
    }

    private void setupClickListeners() {
        // Xử lý sự kiện khi click vào thông tin cá nhân
        layoutPersonalInfo.setOnClickListener(v -> {
            // Mở màn hình thông tin cá nhân
            Intent intent = new Intent(requireContext(), PersonalInfoActivity.class);
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
            // Xác nhận đăng xuất
            Toast.makeText(requireContext(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show();

            // Xóa thông tin đăng nhập
            sessionManager.clearSession();

            // Chuyển về màn hình đăng nhập
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish();
        });
    }
}