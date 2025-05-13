package com.example.cinema_booking_mobile.activity;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.cinema_booking_mobile.MainActivity;
import com.example.cinema_booking_mobile.R;
import com.example.cinema_booking_mobile.dto.request.GoogleLoginRequest;
import com.example.cinema_booking_mobile.dto.request.LoginRequest;
import com.example.cinema_booking_mobile.dto.response.LoginResponse;
import com.example.cinema_booking_mobile.service.IAuthService;
import com.example.cinema_booking_mobile.util.ApiUtils;
import com.example.cinema_booking_mobile.util.NetworkUtil;
import com.example.cinema_booking_mobile.util.SessionManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister, tvForgotPassword;
    private ProgressBar progressBar;
    private ImageButton backButton, togglePasswordVisibility;
    private CardView googleLoginButton, facebookLoginButton;
    private IAuthService authService;
    private SessionManager sessionManager;
    private boolean isPasswordVisible = false;

    // Firebase và Google Sign-In
    private FirebaseAuth mAuth;
    private GoogleSignInClient googleSignInClient;

    // Khai báo lại ActivityResultLauncher với log chi tiết
    private final ActivityResultLauncher<Intent> googleSignInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Log.d("GoogleSignIn", "==== GOOGLE SIGN-IN RESULT RECEIVED ====");
                Log.d("GoogleSignIn", "Result code: " + result.getResultCode());
                // Log để xác nhận RESULT_OK
                Log.d("GoogleSignIn", "RESULT_OK value: " + RESULT_OK);

                // Đây là điểm lỗi! RESULT_OK = 0 trong Android!
                if (result.getResultCode() == RESULT_OK || result.getResultCode() == 0) {
                    Log.d("GoogleSignIn", "Sign-in result OK, getting account info");
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    handleGoogleSignInResult(task);
                } else {
                    Log.e("GoogleSignIn", "Sign-in result NOT OK. Code: " + result.getResultCode());
                    showLoading(false);
                    Toast.makeText(LoginActivity.this, "Đăng nhập Google bị hủy hoặc thất bại", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Khởi tạo các view
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        progressBar = findViewById(R.id.progressBar);
        backButton = findViewById(R.id.backButton);
        togglePasswordVisibility = findViewById(R.id.togglePasswordVisibility);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        googleLoginButton = findViewById(R.id.googleLoginButton);
        facebookLoginButton = findViewById(R.id.facebookLoginButton);

        authService = ApiUtils.getAuthService();
        sessionManager = new SessionManager(this);

        // Khởi tạo Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Cấu hình Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        if (sessionManager.isLoggedIn()) {
            navigateToMainActivity();
        }

        // Xử lý sự kiện nút quay lại
        backButton.setOnClickListener(v -> finish());

        // Xử lý sự kiện hiển thị/ẩn mật khẩu
        togglePasswordVisibility.setOnClickListener(v -> {
            if (isPasswordVisible) {
                // Ẩn mật khẩu
                etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                togglePasswordVisibility.setImageResource(R.drawable.ic_visibility_off);
                isPasswordVisible = false;
            } else {
                // Hiển thị mật khẩu
                etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                togglePasswordVisibility.setImageResource(R.drawable.ic_visibility);
                isPasswordVisible = true;
            }
            // Đặt con trỏ ở cuối văn bản
            etPassword.setSelection(etPassword.getText().length());
        });

        // Xử lý sự kiện đăng nhập
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (validateInput(email, password)) {
                performLogin(email, password);
//                navigateToMainActivity();
            }
        });

        // Xử lý sự kiện đăng ký
        tvRegister.setOnClickListener(v -> {
            // Chuyển đến màn hình đăng ký
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Xử lý sự kiện quên mật khẩu
        tvForgotPassword.setOnClickListener(v -> {
            Toast.makeText(LoginActivity.this, "Chức năng quên mật khẩu đang được phát triển", Toast.LENGTH_SHORT).show();
        });

        // Xử lý đăng nhập với Google
        googleLoginButton.setOnClickListener(v -> {
            if (NetworkUtil.isNetworkAvailable(this)) {
                signInWithGoogle();
            } else {
                Toast.makeText(this, "Vui lòng kiểm tra kết nối internet", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý đăng nhập với Facebook
        facebookLoginButton.setOnClickListener(v -> {
            Toast.makeText(LoginActivity.this, "Đăng nhập với Facebook đang phát triển", Toast.LENGTH_SHORT).show();
        });
    }

    private boolean validateInput(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Vui lòng nhập email");
            return false;
        }

        // Thêm kiểm tra định dạng email
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email không hợp lệ");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Vui lòng nhập mật khẩu");
            return false;
        }

        // Thêm kiểm tra độ dài mật khẩu
        if (password.length() < 6) {
            etPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            return false;
        }

        return true;
    }

    private void performLogin(String email, String password) {
        showLoading(true);

        LoginRequest loginRequest = new LoginRequest(email, password);

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

    private void signInWithGoogle() {
        try {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            googleSignInLauncher.launch(signInIntent);
        } catch (Exception e) {
            Log.e("GoogleSignIn", "Error starting Google Sign-In", e);
            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Thêm logging chi tiết
            String idToken = account.getIdToken();
            Log.d("GoogleSignIn", "ID Token: " + (idToken != null ? idToken.substring(0, 10) + "..." : "null"));
            Log.d("GoogleSignIn", "Email: " + account.getEmail());
            Log.d("GoogleSignIn", "Name: " + account.getDisplayName());

            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            // Thêm log chi tiết về lỗi
            String errorMsg = "Google sign in failed - Status code: " + e.getStatusCode();
            Log.e("GoogleSignIn", errorMsg, e);
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        showLoading(true);
        Log.d(TAG, "Starting Firebase auth with Google");

        // Lấy credential từ Google
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        // Đăng nhập vào Firebase
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Đăng nhập Firebase thành công
                        Log.d(TAG, "Firebase auth successful");
                        // Lấy thông tin người dùng từ Firebase
                        String idToken = acct.getIdToken();
                        String email = acct.getEmail();
                        String name = acct.getDisplayName();
                        String photoUrl = acct.getPhotoUrl() != null ? acct.getPhotoUrl().toString() : null;

                        // Gửi thông tin đến server
                        sendGoogleLoginToServer(idToken, email, name, photoUrl);
                    } else {
                        // Đăng nhập Firebase thất bại
                        Log.e(TAG, "Firebase auth failed", task.getException());
                        showLoading(false);
                        Toast.makeText(LoginActivity.this, "Xác thực Google thất bại: " +
                                        (task.getException() != null ? task.getException().getMessage() : "Lỗi không xác định"),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void sendGoogleLoginToServer(String idToken, String email, String name, String photoUrl) {
        Log.d(TAG, "Preparing to send Google login data to server");

        try {
            if (idToken == null || idToken.isEmpty()) {
                throw new IllegalArgumentException("ID Token cannot be empty");
            }

            // Kiểm tra trạng thái kết nối
            if (!NetworkUtil.isNetworkAvailable(this)) {
                throw new IOException("No network connection available");
            }

            // Log chi tiết về yêu cầu
            Log.d(TAG, "Google login request - Email: " + email);
            Log.d(TAG, "Google login request - Name: " + name);
            Log.d(TAG, "Google login request - Token length: " + idToken.length());

            // Tạo request
            GoogleLoginRequest request = new GoogleLoginRequest(idToken, email, name, photoUrl);

            // Kiểm tra API client
            Log.d(TAG, "Auth Service: " + authService);
            Log.d(TAG, "Session Manager initialized: " + (sessionManager != null));

            // Thêm log chi tiết cho yêu cầu HTTP
            authService.loginWithGoogle(request).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    showLoading(false);

                    Log.d(TAG, "Google login response received - Code: " + response.code());
                    Log.d(TAG, "Request URL: " + call.request().url());

                    if (response.isSuccessful() && response.body() != null) {
                        LoginResponse loginResponse = response.body();

                        // Log về dữ liệu nhận được
                        Log.d(TAG, "Login successful - User ID: " + loginResponse.getId());
                        Log.d(TAG, "Login successful - Token received (length): " +
                                (loginResponse.getToken() != null ? loginResponse.getToken().length() : 0));

                        // Lưu thông tin đăng nhập
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
                        String errorMessage = "Đăng nhập thất bại - HTTP " + response.code();

                        // Xử lý chi tiết mã lỗi HTTP
                        if (response.code() == 401) {
                            errorMessage = "Xác thực không thành công (401)";
                        } else if (response.code() == 403) {
                            errorMessage = "Không có quyền truy cập (403)";
                        } else if (response.code() == 404) {
                            errorMessage = "Không tìm thấy API endpoint (404)";
                        } else if (response.code() >= 500) {
                            errorMessage = "Lỗi máy chủ (" + response.code() + ")";
                        }

                        // Thêm nội dung lỗi từ server nếu có
                        if (response.errorBody() != null) {
                            try {
                                String errorBody = response.errorBody().string();
                                Log.e(TAG, "Error body: " + errorBody);
                                errorMessage += " - " + errorBody;
                            } catch (Exception e) {
                                Log.e(TAG, "Error reading error body", e);
                            }
                        }

                        Log.e(TAG, errorMessage);
                        Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    showLoading(false);

                    // Log chi tiết về lỗi
                    Log.e(TAG, "Connection error during Google login", t);
                    Log.e(TAG, "Failed request URL: " + call.request().url());
                    Log.e(TAG, "Failed request method: " + call.request().method());

                    String errorMessage = "Lỗi kết nối đến máy chủ";
                    if (t instanceof UnknownHostException) {
                        errorMessage = "Không thể kết nối đến máy chủ - Kiểm tra URL và kết nối mạng";
                    } else if (t instanceof SocketTimeoutException) {
                        errorMessage = "Kết nối đến máy chủ quá thời gian chờ";
                    } else if (t instanceof SSLException) {
                        errorMessage = "Lỗi bảo mật kết nối (SSL)";
                    } else {
                        errorMessage = "Lỗi kết nối: " + t.getMessage();
                    }

                    Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            showLoading(false);
            Log.e(TAG, "Exception during Google login process", e);
            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


}