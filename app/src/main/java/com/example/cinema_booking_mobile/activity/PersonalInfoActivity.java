package com.example.cinema_booking_mobile.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cinema_booking_mobile.R;
import com.example.cinema_booking_mobile.util.SessionManager;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalInfoActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private CircleImageView ivUserAvatar;
    private ImageView btnBack, btnChangeAvatar;
    private EditText etFullName, etEmail, etPhone, etBirthday, etAddress;
    private Spinner spinnerGender;
    private Button btnSave;

    private Uri imageUri;
    private SessionManager sessionManager;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        // Khởi tạo SessionManager
        sessionManager = new SessionManager(this);
        calendar = Calendar.getInstance();

        // Ánh xạ các view
        initViews();

        // Cài đặt adapter cho Spinner giới tính
        setupGenderSpinner();

        // Hiển thị thông tin người dùng từ SessionManager
        displayUserInfo();

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

    private void displayUserInfo() {
        // Hiển thị thông tin từ SessionManager
        String userName = sessionManager.getUserName();
        String userEmail = sessionManager.getUserEmail();
        String userPhone = sessionManager.getUserPhone();
        String userBirthday = sessionManager.getUserBirthday();
        String userGender = sessionManager.getUserGender();
        String userAddress = sessionManager.getUserAddress();
        String userAvatar = sessionManager.getUserAvatar();

        // Đặt giá trị vào các trường
        if (userName != null) {
            etFullName.setText(userName);
        }

        if (userEmail != null) {
            etEmail.setText(userEmail);
        }

        if (userPhone != null) {
            etPhone.setText(userPhone);
        }

        if (userBirthday != null) {
            etBirthday.setText(userBirthday);
        }

        if (userAddress != null) {
            etAddress.setText(userAddress);
        }

        // Hiển thị avatar nếu có
        if (userAvatar != null && !userAvatar.isEmpty()) {
            Picasso.get().load(userAvatar).placeholder(R.drawable.ic_person).into(ivUserAvatar);
        }

        // Đặt giới tính trong spinner
        if (userGender != null) {
            ArrayAdapter adapter = (ArrayAdapter) spinnerGender.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++) {
                if (adapter.getItem(i).toString().equals(userGender)) {
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
        btnSave.setOnClickListener(v -> saveUserInfo());
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

    private void saveUserInfo() {
        // Lấy giá trị từ các trường
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String birthday = etBirthday.getText().toString().trim();
        String gender = spinnerGender.getSelectedItem().toString();
        String address = etAddress.getText().toString().trim();

        // Kiểm tra thông tin hợp lệ
        if (fullName.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin bắt buộc", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lưu thông tin vào SessionManager
        sessionManager.setUserName(fullName);
        sessionManager.setUserEmail(email);
        sessionManager.setUserPhone(phone);
        sessionManager.setUserBirthday(birthday);
        sessionManager.setUserGender(gender);
        sessionManager.setUserAddress(address);

        // Hiển thị thông báo thành công
        Toast.makeText(this, "Đã lưu thông tin thành công", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            // Hiển thị ảnh đã chọn
            Picasso.get().load(imageUri).into(ivUserAvatar);

            // Lưu URI ảnh vào SessionManager
            sessionManager.setUserAvatar(imageUri.toString());
        }
    }
}