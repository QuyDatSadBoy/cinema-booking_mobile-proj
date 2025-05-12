package com.example.cinema_booking_mobile.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cinema_booking_mobile.R;
import com.example.cinema_booking_mobile.util.ChatbotPreferences;

public class ChatbotSettingsFragment extends Fragment {

    private ImageButton btnBack;
    private EditText etCinemaName;
    private EditText etCinemaLocation;
    private EditText etCinemaDescription;
    private EditText etTicketPrices;
    private EditText etOpeningHours;
    private EditText etFacilities;
    private EditText etCurrentMovies;
    private EditText etUpcomingMovies;
    private EditText etSpecialPromotions;
    private Button btnSaveSettings;

    private ChatbotPreferences chatbotPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chatbot_settings, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        btnBack = view.findViewById(R.id.btnBack);
        etCinemaName = view.findViewById(R.id.etCinemaName);
        etCinemaLocation = view.findViewById(R.id.etCinemaLocation);
        etCinemaDescription = view.findViewById(R.id.etCinemaDescription);
        etTicketPrices = view.findViewById(R.id.etTicketPrices);
        etOpeningHours = view.findViewById(R.id.etOpeningHours);
        etFacilities = view.findViewById(R.id.etFacilities);
        etCurrentMovies = view.findViewById(R.id.etCurrentMovies);
        etUpcomingMovies = view.findViewById(R.id.etUpcomingMovies);
        etSpecialPromotions = view.findViewById(R.id.etSpecialPromotions);
        btnSaveSettings = view.findViewById(R.id.btnSaveSettings);

        // Initialize preferences
        chatbotPreferences = new ChatbotPreferences(requireContext());

        // Load existing settings
        loadSettings();

        // Setup click listeners
        setupClickListeners();
    }

    private void loadSettings() {
        etCinemaName.setText(chatbotPreferences.getCinemaName());
        etCinemaLocation.setText(chatbotPreferences.getCinemaLocation());
        etCinemaDescription.setText(chatbotPreferences.getCinemaDescription());
        etTicketPrices.setText(chatbotPreferences.getTicketPrices());
        etOpeningHours.setText(chatbotPreferences.getOpeningHours());
        etFacilities.setText(chatbotPreferences.getFacilities());
        etCurrentMovies.setText(chatbotPreferences.getCurrentMovies());
        etUpcomingMovies.setText(chatbotPreferences.getUpcomingMovies());
        etSpecialPromotions.setText(chatbotPreferences.getSpecialPromotions());
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> {
            // Navigate back to chatbot fragment
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        btnSaveSettings.setOnClickListener(v -> saveSettings());
    }

    private void saveSettings() {
        // Lấy tên rạp cũ
        String oldCinemaName = chatbotPreferences.getCinemaName();
        String newCinemaName = etCinemaName.getText().toString().trim();

        // Lưu cài đặt mới
        chatbotPreferences.setCinemaName(newCinemaName);

        // Save all settings to preferences
        chatbotPreferences.setCinemaName(etCinemaName.getText().toString().trim());
        chatbotPreferences.setCinemaLocation(etCinemaLocation.getText().toString().trim());
        chatbotPreferences.setCinemaDescription(etCinemaDescription.getText().toString().trim());
        chatbotPreferences.setTicketPrices(etTicketPrices.getText().toString().trim());
        chatbotPreferences.setOpeningHours(etOpeningHours.getText().toString().trim());
        chatbotPreferences.setFacilities(etFacilities.getText().toString().trim());
        chatbotPreferences.setCurrentMovies(etCurrentMovies.getText().toString().trim());
        chatbotPreferences.setUpcomingMovies(etUpcomingMovies.getText().toString().trim());
        chatbotPreferences.setSpecialPromotions(etSpecialPromotions.getText().toString().trim());

        // Nếu tên rạp thay đổi, xóa lịch sử chat để có tin nhắn chào mới
        if (!oldCinemaName.equals(newCinemaName)) {
            SharedPreferences prefs = requireActivity().getSharedPreferences("ChatbotPrefs", Context.MODE_PRIVATE);
            prefs.edit().remove("chatbot_messages").apply();
            Toast.makeText(requireContext(), "Đã cập nhật tên rạp và làm mới trò chuyện", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Đã lưu thông tin rạp phim", Toast.LENGTH_SHORT).show();
        }
        // Navigate back to chatbot fragment
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}