package com.example.cinema_booking_mobile.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cinema_booking_mobile.R;
import com.example.cinema_booking_mobile.dto.request.UserProfileUpdateRequest;
import com.example.cinema_booking_mobile.dto.response.AvatarResponse;
import com.example.cinema_booking_mobile.dto.response.MessageResponse;
import com.example.cinema_booking_mobile.model.UserProfile;
import com.example.cinema_booking_mobile.service.IProfileService;
import com.example.cinema_booking_mobile.util.ApiUtils;
import com.example.cinema_booking_mobile.util.DateFormatter;
import com.example.cinema_booking_mobile.util.FileUtil;
import com.example.cinema_booking_mobile.util.NetworkUtil;
import com.example.cinema_booking_mobile.util.SessionManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonaInfoActivity extends AppCompatActivity {

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
    private ProgressBar progressBar;

    private Uri imageUri;
    private Calendar calendar;
    private SessionManager sessionManager;
    private SharedPreferences userInfoPreferences;
    private UserProfile userProfile;
    private IProfileService profileService;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        // Khởi tạo
        sessionManager = new SessionManager(this);
        userInfoPreferences = getSharedPreferences(PREF_USER_INFO, Context.MODE_PRIVATE);
        calendar = Calendar.getInstance();
        userProfile = new UserProfile();
        profileService = ApiUtils.getProfileService(); // Sử dụng ProfileService

        // Ánh xạ các view
        initViews();

        // Cài đặt adapter cho Spinner giới tính
        setupGenderSpinner();


        // Kiểm tra xem người dùng đã đăng nhập bằng Google hay chưa
        GoogleSignInAccount googleAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (googleAccount != null) {
            // Sử dụng thông tin từ tài khoản Google nếu có
            String googlePhotoUrl = googleAccount.getPhotoUrl() != null ?
                    googleAccount.getPhotoUrl().toString() : null;
            if (googlePhotoUrl != null && !googlePhotoUrl.isEmpty()) {
                Picasso.get().load(googlePhotoUrl).into(ivUserAvatar);
                userProfile.setAvatarUrl(googlePhotoUrl);
            }
        }

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
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupGenderSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.gender_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);
    }

    private void loadUserProfile() {
        // Kiểm tra kết nối internet
        if (NetworkUtil.isNetworkAvailable(this)) {
            // Hiển thị loading
            showLoading(true);

            // Gọi API lấy thông tin người dùng
            String token = sessionManager.getAuthorizationHeader();

            // Debug để kiểm tra token
            Log.d(TAG, "Token: " + token);

            profileService.getUserProfile(token).enqueue(new Callback<UserProfile>() {
                @Override
                public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                    showLoading(false);

                    if (response.isSuccessful() && response.body() != null) {
                        // Lưu thông tin người dùng
                        userProfile = response.body();

                        // Hiển thị thông tin lên giao diện
                        displayUserInfo();

                        // Lưu thông tin vào SharedPreferences
                        saveUserInfoToCache();
                    } else {
                        // Nếu API lỗi, lấy thông tin từ cache
                        Toast.makeText(PersonaInfoActivity.this,
                                "Không thể lấy thông tin từ server", Toast.LENGTH_SHORT).show();
                        loadUserProfileFromCache();
                    }
                }

                @Override
                public void onFailure(Call<UserProfile> call, Throwable t) {
                    showLoading(false);
                    Toast.makeText(PersonaInfoActivity.this,
                            "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();

                    // Lấy thông tin từ cache khi không kết nối được
                    loadUserProfileFromCache();
                }
            });
        } else {
            // Không có kết nối internet, lấy từ cache
            Toast.makeText(this, "Không có kết nối internet, hiển thị dữ liệu offline", Toast.LENGTH_SHORT).show();
            loadUserProfileFromCache();
        }
    }

    private void saveUserInfoToCache() {
        SharedPreferences.Editor editor = userInfoPreferences.edit();
        if (userProfile.getAvatarUrl() != null) {
            editor.putString(KEY_USER_AVATAR, userProfile.getAvatarUrl());
        }
        if (userProfile.getPhone() != null) {
            editor.putString(KEY_USER_PHONE, userProfile.getPhone());
        }
        if (userProfile.getBirthday() != null) {
            editor.putString(KEY_USER_BIRTHDAY, userProfile.getBirthday());
        }
        if (userProfile.getGender() != null) {
            editor.putString(KEY_USER_GENDER, userProfile.getGender());
        }
        if (userProfile.getAddress() != null) {
            editor.putString(KEY_USER_ADDRESS, userProfile.getAddress());
        }
        editor.apply();
    }

    private void loadUserProfileFromCache() {
        // Lấy thông tin cơ bản từ SessionManager (thông tin đăng nhập)
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

        if (userProfile.getBirthday() != null && !userProfile.getBirthday().isEmpty()) {
            etBirthday.setText(DateFormatter.apiToDisplayFormat(userProfile.getBirthday()));
        }

        if (userProfile.getAddress() != null) {
            etAddress.setText(userProfile.getAddress());
        }

        // Hiển thị avatar nếu có
        if (userProfile.getAvatarUrl() != null && !userProfile.getAvatarUrl().isEmpty()) {
            String avatarUrl = userProfile.getAvatarUrl();

            // Kiểm tra xem URL có phải là URL đầy đủ hay không
            if (!avatarUrl.startsWith("http")) {
                // Nếu là URL tương đối, ghép với base URL
                String baseUrl = "http://10.0.2.2:8080/api"; // URL cho emulator Android
                avatarUrl = baseUrl + avatarUrl;
            }

            Picasso.get()
                    .load(avatarUrl)
                    .placeholder(R.drawable.ic_person)
                    .error(R.drawable.ic_person)
                    .into(ivUserAvatar);
        } else {
            ivUserAvatar.setImageResource(R.drawable.ic_person);
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
        // Nếu đã có ngày sinh, khởi tạo calendar với ngày sinh đó
        if (userProfile.getBirthday() != null && !userProfile.getBirthday().isEmpty()) {
            String[] parts = userProfile.getBirthday().split("-");
            if (parts.length == 3) {
                int year = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]) - 1; // Tháng trong Calendar bắt đầu từ 0
                int day = Integer.parseInt(parts[2]);
                calendar.set(year, month, day);
            }
        }

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
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(dateFormat, Locale.getDefault());
        etBirthday.setText(sdf.format(calendar.getTime()));
    }

    private void saveUserProfile() {
        // Kiểm tra thông tin hợp lệ
        if (!validateUserInput()) {
            return;
        }

        // Kiểm tra kết nối internet
        if (!NetworkUtil.isNetworkAvailable(this)) {
            Toast.makeText(this, "Không có kết nối internet", Toast.LENGTH_SHORT).show();

            // Lưu vào cache cho dùng offline
            userProfile.setName(etFullName.getText().toString().trim());
            userProfile.setPhone(etPhone.getText().toString().trim());
            userProfile.setBirthday(DateFormatter.displayToApiFormat(etBirthday.getText().toString().trim()));
            userProfile.setGender(spinnerGender.getSelectedItem().toString());
            userProfile.setAddress(etAddress.getText().toString().trim());

            saveUserInfoToCache();
            Toast.makeText(this, "Đã lưu thông tin offline", Toast.LENGTH_SHORT).show();
            return;
        }

        // Hiển thị loading
        showLoading(true);

        // Tạo request object
        UserProfileUpdateRequest request = new UserProfileUpdateRequest();
        request.setTen(etFullName.getText().toString().trim());
        request.setSoDienThoai(etPhone.getText().toString().trim());
        request.setNgaySinh(DateFormatter.displayToApiFormat(etBirthday.getText().toString().trim()));
        request.setGioiTinh(spinnerGender.getSelectedItem().toString());
        request.setDiaChi(etAddress.getText().toString().trim());

        // Cập nhật lại thông tin vào userProfile để lưu cache
        userProfile.setName(request.getTen());
        userProfile.setPhone(request.getSoDienThoai());
        userProfile.setBirthday(request.getNgaySinh());
        userProfile.setGender(request.getGioiTinh());
        userProfile.setAddress(request.getDiaChi());

        // Gọi API cập nhật thông tin người dùng
        String token = sessionManager.getAuthorizationHeader();
        profileService.updateUserProfile(token, request).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    // Lưu thông tin vào cache
                    saveUserInfoToCache();

                    Toast.makeText(PersonaInfoActivity.this,
                            "Đã lưu thông tin thành công", Toast.LENGTH_SHORT).show();
//                    finish();
                } else {
                    Toast.makeText(PersonaInfoActivity.this,
                            "Lỗi cập nhật thông tin: " + (response.errorBody() != null ?
                                    response.errorBody().toString() : response.message()),
                            Toast.LENGTH_SHORT).show();

                    // Vẫn lưu vào cache cho dùng offline
                    saveUserInfoToCache();
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                showLoading(false);
                Toast.makeText(PersonaInfoActivity.this,
                        "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();

                // Lưu vào cache cho dùng offline
                saveUserInfoToCache();
                Toast.makeText(PersonaInfoActivity.this,
                        "Đã lưu thông tin offline", Toast.LENGTH_SHORT).show();
            }
        });
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

            // Kiểm tra kết nối internet
            if (NetworkUtil.isNetworkAvailable(this)) {
                // Upload ảnh lên server
                uploadAvatar(imageUri);
            } else {
                // Lưu URI vào cache để upload sau
                userProfile.setAvatarUrl(imageUri.toString());
                SharedPreferences.Editor editor = userInfoPreferences.edit();
                editor.putString(KEY_USER_AVATAR, imageUri.toString());
                editor.apply();
                Toast.makeText(this, "Đã lưu avatar offline", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadAvatar(Uri imageUri) {
        try {
            // Hiển thị loading
            showLoading(true);

            // Lấy file từ Uri
            File file = FileUtil.getFileFromUri(this, imageUri);
            if (file == null) {
                showLoading(false);
                Toast.makeText(this, "Không thể xử lý file ảnh", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tạo RequestBody từ file
            String mimeType = getContentResolver().getType(imageUri);
            if (mimeType == null) {
                mimeType = "image/*";
            }

            RequestBody requestFile = RequestBody.create(
                    MediaType.parse(mimeType), file);

            // Tạo MultipartBody.Part
            MultipartBody.Part body = MultipartBody.Part.createFormData(
                    "file", file.getName(), requestFile);

            // Gọi API upload avatar
            String token = sessionManager.getAuthorizationHeader();
            profileService.uploadAvatar(token, body).enqueue(new Callback<AvatarResponse>() {
                @Override
                public void onResponse(Call<AvatarResponse> call, Response<AvatarResponse> response) {
                    showLoading(false);

                    if (response.isSuccessful() && response.body() != null) {
                        String avatarUrl = response.body().getAvatarUrl();

                        // Cập nhật avatar URL trong userProfile
                        userProfile.setAvatarUrl(avatarUrl);

                        // Lưu URL avatar vào cache
                        SharedPreferences.Editor editor = userInfoPreferences.edit();
                        editor.putString(KEY_USER_AVATAR, avatarUrl);
                        editor.apply();

                        // Cập nhật SessionManager (nếu cần)
                        sessionManager.setUserAvatar(avatarUrl);

                        Toast.makeText(PersonaInfoActivity.this,
                                "Upload avatar thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PersonaInfoActivity.this,
                                "Lỗi upload avatar: " + (response.errorBody() != null ?
                                        response.errorBody().toString() : response.message()),
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<AvatarResponse> call, Throwable t) {
                    showLoading(false);
                    Toast.makeText(PersonaInfoActivity.this,
                            "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();

                    // Lưu URI local vào cache
                    userProfile.setAvatarUrl(imageUri.toString());
                    SharedPreferences.Editor editor = userInfoPreferences.edit();
                    editor.putString(KEY_USER_AVATAR, imageUri.toString());
                    editor.apply();
                    Toast.makeText(PersonaInfoActivity.this,
                            "Đã lưu avatar offline", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            showLoading(false);
            Log.e(TAG, "Error uploading avatar: " + e.getMessage());
            Toast.makeText(this, "Lỗi xử lý file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showLoading(boolean isLoading) {
        this.isLoading = isLoading;
        runOnUiThread(() -> {
            if (isLoading) {
                progressBar.setVisibility(View.VISIBLE);
                btnSave.setEnabled(false);
            } else {
                progressBar.setVisibility(View.GONE);
                btnSave.setEnabled(true);
            }
        });
    }


    @Override
    public void onBackPressed() {
        // Đặt result để báo cho Activity trước biết cần refresh data
        setResult(RESULT_OK);
        super.onBackPressed();
    }


}