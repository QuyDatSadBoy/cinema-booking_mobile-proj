package com.example.cinema_booking_mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinema_booking_mobile.MainActivity;
import com.example.cinema_booking_mobile.R;
import com.example.cinema_booking_mobile.dto.request.LoginRequest;
import com.example.cinema_booking_mobile.dto.response.LoginResponse;
import com.example.cinema_booking_mobile.service.IAuthService;
import com.example.cinema_booking_mobile.util.ApiUtils;
import com.example.cinema_booking_mobile.util.SessionManager;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private ProgressBar progressBar;
    private IAuthService authService;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        progressBar = findViewById(R.id.progressBar);

        authService = ApiUtils.getAuthService();
        sessionManager = new SessionManager(this);

        if (sessionManager.isLoggedIn()) {
            navigateToMainActivity();
        }

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (validateInput(email, password)) {
                performLogin(email, password);
            }
        });

        tvRegister.setOnClickListener(v -> {
            Toast.makeText(LoginActivity.this, "Chức năng đăng ký đang được phát triển", Toast.LENGTH_SHORT).show();
        });
    }

    private boolean validateInput(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Vui lòng nhập email");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Vui lòng nhập mật khẩu");
            return false;
        }

        return true;
    }

    private void performLogin(String email, String password) {
        showLoading(true);

        LoginRequest loginRequest = new LoginRequest(email, password);
        Toast.makeText(getApplicationContext(),email,Toast.LENGTH_LONG);

        authService.login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();

                    sessionManager.saveAuthToken(loginResponse.getToken(), loginResponse.getRefreshToken());

                    sessionManager.saveUserInfo(
                            loginResponse.getId(),
                            loginResponse.getEmail(),
                            loginResponse.getTen(),
                            loginResponse.getRole()
                    );

                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    navigateToMainActivity();
                } else {
                    String errorMessage = "Đăng nhập thất bại. Vui lòng kiểm tra lại thông tin.";
                    if (response.errorBody() != null) {
                        try {
                            errorMessage = response.errorBody().string();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                showLoading(false);
                Toast.makeText(LoginActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        btnLogin.setEnabled(!isLoading);
        etEmail.setEnabled(!isLoading);
        etPassword.setEnabled(!isLoading);
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}