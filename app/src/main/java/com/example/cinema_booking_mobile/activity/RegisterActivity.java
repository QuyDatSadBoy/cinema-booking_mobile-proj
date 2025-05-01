package com.example.cinema_booking_mobile.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.cinema_booking_mobile.MainActivity;
import com.example.cinema_booking_mobile.R;
import com.example.cinema_booking_mobile.dto.request.SignupRequest;
import com.example.cinema_booking_mobile.dto.response.LoginResponse;
import com.example.cinema_booking_mobile.service.IAuthService;
import com.example.cinema_booking_mobile.util.ApiUtils;
import com.example.cinema_booking_mobile.util.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword, etConfirmPassword, etPhone, etAddress;
    private Spinner spinnerGender;
    private Button btnRegister, btnDatePicker;
    private CheckBox cbTerms;
    private TextView tvLogin, tvTermsLink, tvPrivacyLink, tvBirthday;
    private ImageButton backButton, togglePasswordVisibility, toggleConfirmPasswordVisibility;
    private CardView googleLoginButton, facebookLoginButton;
    private ProgressBar progressBar;
    private IAuthService authService;
    private SessionManager sessionManager;
    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;
    private Calendar calendar;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        // Khởi tạo view
        initViews();

        // Khởi tạo service và session manager
        authService = ApiUtils.getAuthService();
        sessionManager = new SessionManager(this);

        // Thiết lập lịch và định dạng ngày
        calendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        // Thiết lập spinner giới tính
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(
                this, R.array.gender_options, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(genderAdapter);

        // Thiết lập sự kiện click
        setupClickListeners();
    }

    @SuppressLint("WrongViewCast")
    private void initViews() {
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        spinnerGender = findViewById(R.id.spinnerGender);
        btnDatePicker = findViewById(R.id.btnDatePicker);
        tvBirthday = findViewById(R.id.tvBirthday);
        btnRegister = findViewById(R.id.btnRegister);
        cbTerms = findViewById(R.id.cbTerms);
        tvLogin = findViewById(R.id.tvLogin);
        tvTermsLink = findViewById(R.id.tvTermsLink);
        tvPrivacyLink = findViewById(R.id.tvPrivacyLink);
        backButton = findViewById(R.id.backButton);
        togglePasswordVisibility = findViewById(R.id.togglePasswordVisibility);
        toggleConfirmPasswordVisibility = findViewById(R.id.toggleConfirmPasswordVisibility);
        googleLoginButton = findViewById(R.id.googleLoginButton);
        facebookLoginButton = findViewById(R.id.facebookLoginButton);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupClickListeners() {
        // Xử lý sự kiện quay lại
        backButton.setOnClickListener(v -> finish());

        // Xử lý sự kiện chọn ngày sinh
        btnDatePicker.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        tvBirthday.setText(dateFormatter.format(calendar.getTime()));
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        // Xử lý sự kiện hiển thị/ẩn mật khẩu
        togglePasswordVisibility.setOnClickListener(v -> {
            togglePasswordVisibility(etPassword, togglePasswordVisibility);
            isPasswordVisible = !isPasswordVisible;
        });

        toggleConfirmPasswordVisibility.setOnClickListener(v -> {
            togglePasswordVisibility(etConfirmPassword, toggleConfirmPasswordVisibility);
            isConfirmPasswordVisible = !isConfirmPasswordVisible;
        });

        // Xử lý sự kiện đăng ký
        btnRegister.setOnClickListener(v -> {
            if (validateInputs()) {
                performRegistration();
            }
        });

        // Xử lý sự kiện chuyển đến trang đăng nhập
        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });

        // Xử lý sự kiện xem điều khoản
        tvTermsLink.setOnClickListener(v -> {
            Toast.makeText(RegisterActivity.this, "Điều khoản sử dụng", Toast.LENGTH_SHORT).show();
        });

        // Xử lý sự kiện xem chính sách
        tvPrivacyLink.setOnClickListener(v -> {
            Toast.makeText(RegisterActivity.this, "Chính sách bảo mật", Toast.LENGTH_SHORT).show();
        });

        // Xử lý đăng nhập với Google
        googleLoginButton.setOnClickListener(v -> {
            Toast.makeText(RegisterActivity.this, "Đăng nhập với Google đang phát triển", Toast.LENGTH_SHORT).show();
        });

        // Xử lý đăng nhập với Facebook
        facebookLoginButton.setOnClickListener(v -> {
            Toast.makeText(RegisterActivity.this, "Đăng nhập với Facebook đang phát triển", Toast.LENGTH_SHORT).show();
        });
    }

    private void togglePasswordVisibility(EditText editText, ImageButton toggleButton) {
        if (editText.getTransformationMethod() instanceof PasswordTransformationMethod) {
            // Hiện mật khẩu
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            toggleButton.setImageResource(R.drawable.ic_visibility);
        } else {
            // Ẩn mật khẩu
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            toggleButton.setImageResource(R.drawable.ic_visibility_off);
        }
        // Đặt con trỏ ở cuối văn bản
        editText.setSelection(editText.getText().length());
    }

    private boolean validateInputs() {
        boolean isValid = true;
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Kiểm tra tên
        if (TextUtils.isEmpty(name)) {
            etName.setError("Vui lòng nhập tên");
            isValid = false;
        }

        // Kiểm tra email
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Vui lòng nhập email");
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email không hợp lệ");
            isValid = false;
        }

        // Kiểm tra mật khẩu
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Vui lòng nhập mật khẩu");
            isValid = false;
        } else if (password.length() < 6) {
            etPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            isValid = false;
        }

        // Kiểm tra xác nhận mật khẩu
        if (TextUtils.isEmpty(confirmPassword)) {
            etConfirmPassword.setError("Vui lòng xác nhận mật khẩu");
            isValid = false;
        } else if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Mật khẩu không khớp");
            isValid = false;
        }

        // Kiểm tra đồng ý điều khoản
        if (!cbTerms.isChecked()) {
            Toast.makeText(this, "Vui lòng đồng ý với điều khoản và chính sách", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        return isValid;
    }

    private void performRegistration() {
        showLoading(true);

        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String birthday = tvBirthday.getText().toString().trim();
        String gender = spinnerGender.getSelectedItem().toString();
        String address = etAddress.getText().toString().trim();

        // Kiểm tra nếu không có dữ liệu để tránh gửi chuỗi rỗng
        if (TextUtils.isEmpty(phone)) phone = null;
        if (TextUtils.isEmpty(birthday)) birthday = null;
        if (TextUtils.isEmpty(address)) address = null;

        SignupRequest signupRequest = new SignupRequest(email, name, password, phone, birthday, gender, address);

        authService.register(signupRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();

                    // Lưu thông tin đăng nhập vào SessionManager
                    sessionManager.saveAuthToken(loginResponse.getToken(), loginResponse.getRefreshToken());
                    sessionManager.saveUserInfo(
                            loginResponse.getId(),
                            loginResponse.getEmail(),
                            loginResponse.getTen(),
                            loginResponse.getRole()
                    );

                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    navigateToMainActivity();
                } else {
                    String errorMessage = "Đăng ký thất bại. Vui lòng kiểm tra lại thông tin.";
                    if (response.errorBody() != null) {
                        try {
                            errorMessage = response.errorBody().string();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                showLoading(false);
                Toast.makeText(RegisterActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        btnRegister.setEnabled(!isLoading);
        etName.setEnabled(!isLoading);
        etEmail.setEnabled(!isLoading);
        etPassword.setEnabled(!isLoading);
        etConfirmPassword.setEnabled(!isLoading);
        etPhone.setEnabled(!isLoading);
        etAddress.setEnabled(!isLoading);
        spinnerGender.setEnabled(!isLoading);
        btnDatePicker.setEnabled(!isLoading);
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}