package com.example.cinema_booking_mobile.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinema_booking_mobile.R;
import com.example.cinema_booking_mobile.adapter.PaymentMethodAdapter;
import com.example.cinema_booking_mobile.dto.response.PaymentMethodDTO;
import com.example.cinema_booking_mobile.model.PaymentMethod;
import com.example.cinema_booking_mobile.service.IPaymentService;
import com.example.cinema_booking_mobile.util.ApiUtils;
import com.example.cinema_booking_mobile.util.NetworkUtil;
import com.example.cinema_booking_mobile.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
            Toast.makeText(this, "Thêm phương thức mới", Toast.LENGTH_SHORT).show();
        });

        initData();

        recyclerViewPaymentMethods.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PaymentMethodAdapter(paymentMethods, this);
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

    @Override
    public void onPaymentMethodClick(PaymentMethod paymentMethod, int position) {
        Toast.makeText(this, "Đã chọn: " + paymentMethod.getName(), Toast.LENGTH_SHORT).show();
    }
}