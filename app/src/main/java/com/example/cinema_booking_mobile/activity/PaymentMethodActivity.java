package com.example.cinema_booking_mobile.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinema_booking_mobile.R;
import com.example.cinema_booking_mobile.adapter.PaymentMethodAdapter;
import com.example.cinema_booking_mobile.dto.response.PaymentMethodDTO;
import com.example.cinema_booking_mobile.model.PaymentMethod;
import com.example.cinema_booking_mobile.service.IPaymentService;
import com.example.cinema_booking_mobile.util.ApiResponse;
import com.example.cinema_booking_mobile.util.ApiUtils;
import com.example.cinema_booking_mobile.util.NetworkUtil;
import com.example.cinema_booking_mobile.util.SessionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class PaymentMethodActivity extends AppCompatActivity implements PaymentMethodAdapter.OnPaymentMethodClickListener {

    private RecyclerView recyclerViewPaymentMethods;
    private PaymentMethodAdapter adapter;
    private List<PaymentMethod> paymentMethods;
    private Button btnAddPaymentMethod;
    private ImageButton btnBack;
    private IPaymentService iPaymentService;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_methods);

        sessionManager = new SessionManager(this);
        iPaymentService = ApiUtils.getPaymentService();

        recyclerViewPaymentMethods = findViewById(R.id.recyclerViewPaymentMethods);
        btnAddPaymentMethod = findViewById(R.id.btnAddPaymentMethod);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> onBackPressed());
        btnAddPaymentMethod.setOnClickListener(v -> {
            showAddPaymentDialog();
            Toast.makeText(this, "Thêm phương thức mới", Toast.LENGTH_SHORT).show();
        });

        initData();

        recyclerViewPaymentMethods.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PaymentMethodAdapter(paymentMethods);
        adapter.setOnPaymentMethodClickListener(this);
        recyclerViewPaymentMethods.setAdapter(adapter);
    }

    private void initData() {
        paymentMethods = new ArrayList<>();

        if (!NetworkUtil.isNetworkAvailable(this)) {
            Toast.makeText(this, "Không có kết nối mạng", Toast.LENGTH_SHORT).show();
            return;
        }
        iPaymentService.getAllPhuongThucThanhToanByKhachHang(sessionManager.getAuthorizationHeader()).enqueue(
                new Callback<List<PaymentMethodDTO>>() {
                    @Override
                    public void onResponse(Call<List<PaymentMethodDTO>> call, Response<List<PaymentMethodDTO>> response) {
                        if(response.isSuccessful() && response.body()!=null){
                            paymentMethods.clear();
                            List<PaymentMethodDTO> paymentMethodDTOS = response.body();
                            for (PaymentMethodDTO paymentMethodDTO:paymentMethodDTOS){
                                paymentMethods.add(new PaymentMethod(
                                        paymentMethodDTO.getId(),
                                        paymentMethodDTO.getTen(),
                                        paymentMethodDTO.getSoTaiKhoan(),
                                        getIconMethod(paymentMethodDTO.getTen())
                                ));
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<PaymentMethodDTO>> call, Throwable t) {

                    }
                }
        );
    }

    @Override
    public void onPaymentMethodClick(int position) {
        showEditPaymentDialog(position);
    }


    private void showAddPaymentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_payment_method, null);
        builder.setView(dialogView);

        Spinner spinnerPaymentType = dialogView.findViewById(R.id.spinnerPaymentType);
        EditText editTextAccountInfo = dialogView.findViewById(R.id.editTextAccountInfo);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnSave = dialogView.findViewById(R.id.btnSave);

        List<String> availablePaymentMethods = new ArrayList<>(Arrays.asList(
                "Momo", "MBBank", "ZaloPay", "Techcombank", "BIDV", "Vietcombank", "VietinBank", "Agribank"
        ));

        for (PaymentMethod pm : paymentMethods) {
            availablePaymentMethods.remove(pm.getName());
        }

        if (availablePaymentMethods.isEmpty()) {
            Toast.makeText(this, "Bạn đã thêm tất cả các phương thức thanh toán", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                availablePaymentMethods
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPaymentType.setAdapter(spinnerAdapter);

        AlertDialog dialog = builder.create();
        dialog.show();

        btnCancel.setOnClickListener(view -> dialog.dismiss());

        btnSave.setOnClickListener(view -> {
            String selectedPaymentType = spinnerPaymentType.getSelectedItem().toString();
            String accountInfo = editTextAccountInfo.getText().toString().trim();

            if (accountInfo.isEmpty()) {
                editTextAccountInfo.setError("Vui lòng nhập số tài khoản");
                return;
            }

            if (!accountInfo.matches("\\d+")) {
                editTextAccountInfo.setError("Số tài khoản chỉ được chứa các chữ số");
                return;
            }

            if (accountInfo.length() != 8) {
                editTextAccountInfo.setError("Số tài khoản phải gồm đúng 8 chữ số");
                return;
            }

            int paymentMethodId = getPaymentMethodId(selectedPaymentType);

            iPaymentService.enablePaymentMethod(
                    sessionManager.getAuthorizationHeader(),
                    paymentMethodId,
                    accountInfo
            ).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                        int logoResId = getIconMethod(selectedPaymentType);

                        Log.d("paymentMethodId", paymentMethodId + "");

                        PaymentMethod newPaymentMethod = new PaymentMethod(
                                paymentMethodId,
                                selectedPaymentType,
                                accountInfo,
                                logoResId
                        );

                        paymentMethods.add(newPaymentMethod);
                        adapter.notifyItemInserted(paymentMethods.size() - 1);

                        Toast.makeText(PaymentMethodActivity.this,
                                "Đã thêm " + selectedPaymentType + " thành công",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PaymentMethodActivity.this, "errorMessage", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Log.e("PaymentMethod", "Error enabling payment method", t);
                    Toast.makeText(PaymentMethodActivity.this,
                            "Lỗi kết nối: " + t.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });

            dialog.dismiss();
        });
    }

    private void showEditPaymentDialog(int position) {
        PaymentMethod paymentMethod = paymentMethods.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_payment_method, null);
        builder.setView(dialogView);

        TextView textViewPaymentName = dialogView.findViewById(R.id.textViewPaymentName);
        EditText editTextAccountNumber = dialogView.findViewById(R.id.editTextAccountNumber);
        RadioGroup radioGroupStatus = dialogView.findViewById(R.id.radioGroupStatus);
        RadioButton radioEnable = dialogView.findViewById(R.id.radioEnable);
        RadioButton radioDisable = dialogView.findViewById(R.id.radioDisable);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnSave = dialogView.findViewById(R.id.btnSave);

        textViewPaymentName.setText(paymentMethod.getName());
        editTextAccountNumber.setText(paymentMethod.getPhoneNumber());

        String currentStatus = paymentMethod.getStatus() != null ? paymentMethod.getStatus() : "ENABLE";
        if (currentStatus.equals("ENABLE")) {
            radioEnable.setChecked(true);
        } else {
            radioDisable.setChecked(true);
        }

        AlertDialog dialog = builder.create();
        dialog.show();

        btnCancel.setOnClickListener(view -> dialog.dismiss());

        btnSave.setOnClickListener(view -> {
            String accountNumber = editTextAccountNumber.getText().toString().trim();
            String status = radioEnable.isChecked() ? "ENABLE" : "DISABLE";

            if (accountNumber.isEmpty()) {
                editTextAccountNumber.setError("Vui lòng nhập số tài khoản");
                return;
            }

            if (!accountNumber.matches("\\d+")) {
                editTextAccountNumber.setError("Số tài khoản chỉ được chứa các chữ số");
                return;
            }

            if (accountNumber.length() != 8) {
                editTextAccountNumber.setError("Số tài khoản phải gồm đúng 8 chữ số");
                return;
            }

            updatePaymentMethod(position, paymentMethod.getId(), accountNumber, status, dialog);
        });
    }

    private void updatePaymentMethod(int position, int paymentMethodId, String accountNumber, String status, AlertDialog dialog) {
        PaymentMethod currentMethod = paymentMethods.get(position);

        PaymentMethodDTO paymentMethodDTO = new PaymentMethodDTO();
        paymentMethodDTO.setId(paymentMethodId);
        paymentMethodDTO.setTen(currentMethod.getName());
        paymentMethodDTO.setSoTaiKhoan(accountNumber);
        paymentMethodDTO.setStatus(status);

        iPaymentService.updatePaymentMethod(
                sessionManager.getAuthorizationHeader(),
                paymentMethodDTO
        ).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null && response.body()) {
                    if (status.equals("DISABLE")) {
                        paymentMethods.remove(position);
                        adapter.notifyItemRemoved(position);
                        Toast.makeText(PaymentMethodActivity.this, "Đã vô hiệu hóa phương thức thanh toán", Toast.LENGTH_SHORT).show();
                    } else {
                        PaymentMethod updatedMethod = paymentMethods.get(position);
                        updatedMethod.setPhoneNumber(accountNumber);
                        updatedMethod.setStatus(status);
                        adapter.notifyItemChanged(position);
                        Toast.makeText(PaymentMethodActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PaymentMethodActivity.this, "Không thể cập nhật phương thức thanh toán", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(PaymentMethodActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }


    private int getPaymentMethodId(String paymentMethodName) {
        switch (paymentMethodName) {
            case "Momo":
                return 1;
            case "MBBank":
                return 2;
            case "ZaloPay":
                return 3;
            case "Techcombank":
                return 4;
            case "BIDV":
                return 5;
            case "Vietcombank":
                return 6;
            case "VietinBank":
                return 7;
            case "Agribank":
                return 8;
            default:
                return 1;
        }
    }

    private int getIconMethod(String name) {
        switch (name) {
            case "Momo":
                return R.drawable.ic_momo;
            case "MBBank":
                return R.drawable.ic_mb_bank;
            case "ZaloPay":
                return R.drawable.ic_zalo_pay;
            case "Techcombank":
                return R.drawable.ic_techcombank;
            case "BIDV":
                return R.drawable.ic_bidv;
            case "Vietcombank":
                return R.drawable.ic_vietcombank;
            case "VietinBank":
                return R.drawable.ic_vietinbank;
            case "Agribank":
                return R.drawable.ic_agribank;
            default:
                return R.drawable.ic_momo;
        }
    }

}