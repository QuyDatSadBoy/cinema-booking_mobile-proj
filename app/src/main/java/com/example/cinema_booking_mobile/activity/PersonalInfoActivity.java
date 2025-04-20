package com.example.cinema_booking_mobile.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cinema_booking_mobile.R;
import com.example.cinema_booking_mobile.model.UserProfile;
import com.example.cinema_booking_mobile.util.SessionManager;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalInfoActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String TAG = "PersonalInfoActivity";
    // Shared Preferences Constants
    private static final String PREF_USER_INFO = "UserInfoPreferences";
    private static final String KEY_USER_AVATAR = "userAvatar";
    private static final String KEY_USER_PHONE = "userPhone";
    private static final String KEY_USER_BIRTHDAY = "userBirthday";
    private static final String KEY_USER_GENDER = "userGender";
    private static final String KEY_USER_ADDRESS = "userAddress";

    private CircleImageView ivUserAvatar;
    private ImageView btnBack, btnChangeAvatar;
    private EditText etFullName, etEmail, etPhone, etBirthday, etAddress;
    private Spinner spinnerGender;
    private Button btnSave;

    private Uri imageUri;
    private Calendar calendar;
    private SessionManager sessionManager;
    private SharedPreferences userInfoPreferences;
    private UserProfile userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        // Khởi tạo
        sessionManager = new SessionManager(this);
        userInfoPreferences = getSharedPreferences(PREF_USER_INFO, Context.MODE_PRIVATE);
        calendar = Calendar.getInstance();
        userProfile = new UserProfile();

        // Ánh xạ các view
        initViews();

        // Cài đặt adapter cho Spinner giới tính
        setupGenderSpinner();

        // Lấy thông tin người dùng
        loadUserProfile();

        // Thiết lập các sự kiện
        setupEventListeners();
    }

    private void initViews() {
        ivUserAvatar = findViewById(R.id.ivUserAvatar);
        btnBack = findViewById(R.id.btnBack);
        btnChangeAvatar = findViewById(R.id.btnChangeAvatar);
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etBirthday = findViewById(R.id.etBirthday);
        etAddress = findViewById(R.id.etAddress);
        spinnerGender = findViewById(R.id.spinnerGender);
        btnSave = findViewById(R.id.btnSave);
    }

    private void setupGenderSpinner() {
        // Tạo adapter cho spinner giới tính
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.gender_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);
    }

    private void loadUserProfile() {
        // Trong tương lai, đây sẽ là nơi để gọi API lấy thông tin người dùng
        // Ví dụ:
        // ApiClient.getApiService().getUserProfile(userId).enqueue(new Callback<UserProfile>() { ... });

        // Hiện tại, lấy thông tin cơ bản từ SessionManager (thông tin đăng nhập)
        userProfile.setId(sessionManager.getUserId());
        userProfile.setName(sessionManager.getUserName());
        userProfile.setEmail(sessionManager.getUserEmail());

        // Lấy thông tin bổ sung từ SharedPreferences
        userProfile.setAvatarUrl(userInfoPreferences.getString(KEY_USER_AVATAR, null));
        userProfile.setPhone(userInfoPreferences.getString(KEY_USER_PHONE, null));
        userProfile.setBirthday(userInfoPreferences.getString(KEY_USER_BIRTHDAY, null));
        userProfile.setGender(userInfoPreferences.getString(KEY_USER_GENDER, null));
        userProfile.setAddress(userInfoPreferences.getString(KEY_USER_ADDRESS, null));

        // Hiển thị dữ liệu lên giao diện
        displayUserInfo();
    }

    private void displayUserInfo() {
        // Hiển thị thông tin lên giao diện
        if (userProfile.getName() != null) {
            etFullName.setText(userProfile.getName());
        }

        if (userProfile.getEmail() != null) {
            etEmail.setText(userProfile.getEmail());
        }

        if (userProfile.getPhone() != null) {
            etPhone.setText(userProfile.getPhone());
        }

        if (userProfile.getBirthday() != null) {
            etBirthday.setText(userProfile.getBirthday());
        }

        if (userProfile.getAddress() != null) {
            etAddress.setText(userProfile.getAddress());
        }

        // Hiển thị avatar nếu có
        if (userProfile.getAvatarUrl() != null && !userProfile.getAvatarUrl().isEmpty()) {
            Picasso.get().load(userProfile.getAvatarUrl()).placeholder(R.drawable.ic_person).into(ivUserAvatar);
        }

        // Đặt giới tính trong spinner
        if (userProfile.getGender() != null) {
            ArrayAdapter adapter = (ArrayAdapter) spinnerGender.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++) {
                if (adapter.getItem(i).toString().equals(userProfile.getGender())) {
                    spinnerGender.setSelection(i);
                    break;
                }
            }
        }
    }

    private void setupEventListeners() {
        // Sự kiện nút quay lại
        btnBack.setOnClickListener(v -> finish());

        // Sự kiện thay đổi ảnh đại diện
        btnChangeAvatar.setOnClickListener(v -> openImagePicker());

        // Sự kiện chọn ngày sinh
        etBirthday.setOnClickListener(v -> showDatePickerDialog());

        // Sự kiện lưu thông tin
        btnSave.setOnClickListener(v -> saveUserProfile());
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateDateLabel();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void updateDateLabel() {
        String dateFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
        etBirthday.setText(sdf.format(calendar.getTime()));
    }

    private void saveUserProfile() {
        // Kiểm tra thông tin hợp lệ
        if (!validateUserInput()) {
            return;
        }

        // Cập nhật thông tin từ form vào đối tượng userProfile
        userProfile.setName(etFullName.getText().toString().trim());
        userProfile.setEmail(etEmail.getText().toString().trim());
        userProfile.setPhone(etPhone.getText().toString().trim());
        userProfile.setBirthday(etBirthday.getText().toString().trim());
        userProfile.setGender(spinnerGender.getSelectedItem().toString());
        userProfile.setAddress(etAddress.getText().toString().trim());

        // Trong tương lai, đây sẽ là nơi để gọi API cập nhật thông tin người dùng
        // Ví dụ:
        // ApiClient.getApiService().updateUserProfile(userId, userProfile).enqueue(new Callback<Response>() { ... });

        // Hiện tại, lưu thông tin vào SharedPreferences
        SharedPreferences.Editor editor = userInfoPreferences.edit();
        editor.putString(KEY_USER_PHONE, userProfile.getPhone());
        editor.putString(KEY_USER_BIRTHDAY, userProfile.getBirthday());
        editor.putString(KEY_USER_GENDER, userProfile.getGender());
        editor.putString(KEY_USER_ADDRESS, userProfile.getAddress());
        editor.apply();

        // Hiển thị thông báo thành công
        Toast.makeText(this, "Đã lưu thông tin thành công", Toast.LENGTH_SHORT).show();
        finish();
    }

    private boolean validateUserInput() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        if (fullName.isEmpty()) {
            etFullName.setError("Vui lòng nhập họ và tên");
            etFullName.requestFocus();
            return false;
        }

        if (email.isEmpty()) {
            etEmail.setError("Vui lòng nhập email");
            etEmail.requestFocus();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Vui lòng nhập email hợp lệ");
            etEmail.requestFocus();
            return false;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            // Hiển thị ảnh đã chọn
            Picasso.get().load(imageUri).into(ivUserAvatar);

            // Trong tương lai, đây sẽ là nơi để gọi API upload ảnh lên server
            // Ví dụ:
            // uploadImageToServer(imageUri);

            // Hiện tại, lưu URI ảnh vào SharedPreferences
            userProfile.setAvatarUrl(imageUri.toString());
            SharedPreferences.Editor editor = userInfoPreferences.edit();
            editor.putString(KEY_USER_AVATAR, imageUri.toString());
            editor.apply();
        }
    }
}