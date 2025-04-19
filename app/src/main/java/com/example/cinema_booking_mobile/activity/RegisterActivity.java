package com.example.cinema_booking_mobile.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.cinema_booking_mobile.R;
import com.example.cinema_booking_mobile.dto.request.SignupRequest;
import com.example.cinema_booking_mobile.service.IAuthService;
import com.example.cinema_booking_mobile.util.ApiUtils;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword, etConfirmPassword;
    private Button btnRegister;
    private CheckBox cbTerms;
    private TextView tvLogin, tvTermsLink, tvPrivacyLink;
    private ImageButton backButton, togglePasswordVisibility, toggleConfirmPasswordVisibility;
    private CardView googleLoginButton, facebookLoginButton;
    private ProgressBar progressBar;
    private IAuthService authService;
    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        // Khởi tạo view
        initViews();

        // Khởi tạo service
        authService = ApiUtils.getAuthService();

        // Thiết lập sự kiện click
        setupClickListeners();
    }

    @SuppressLint("WrongViewCast")
    private void initViews() {
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
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
                // Logic đăng ký sẽ được thêm sau
                Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
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

    private void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        btnRegister.setEnabled(!isLoading);
        etName.setEnabled(!isLoading);
        etEmail.setEnabled(!isLoading);
        etPassword.setEnabled(!isLoading);
        etConfirmPassword.setEnabled(!isLoading);
    }
}