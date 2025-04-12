package com.example.cinema_booking_mobile.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinema_booking_mobile.R;
import com.example.cinema_booking_mobile.adapter.OnItemClickListener;
import com.example.cinema_booking_mobile.adapter.PaymentMethodsAdapter;
import com.example.cinema_booking_mobile.model.PaymentMethod;

import java.util.ArrayList;
import java.util.List;

public class PaymentDetailsActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private ImageView imgPoster;
    private TextView tvMovieTitle, tvDateTime, tvRoom, tvSeats;
    private TextView tvTicketPrice, tvVat, tvTotalPrice;
    private RecyclerView rvPaymentMethods;
    private Button btnConfirmPayment;

    private PaymentMethodsAdapter paymentMethodsAdapter;
    private PaymentMethod selectedPaymentMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        initViews();
        setupPaymentMethodsAdapter();
        setupListeners();
        loadData();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);

        imgPoster = findViewById(R.id.imgPoster);
        tvMovieTitle = findViewById(R.id.tvMovieTitle);
        tvDateTime = findViewById(R.id.tvDateTime);
        tvRoom = findViewById(R.id.tvRoom);
        tvSeats = findViewById(R.id.tvSeats);

        tvTicketPrice = findViewById(R.id.tvTicketPrice);
        tvVat = findViewById(R.id.tvVat);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);

        rvPaymentMethods = findViewById(R.id.rvPaymentMethods);

        btnConfirmPayment = findViewById(R.id.btnConfirmPayment);
    }

    private void setupPaymentMethodsAdapter() {
        List<PaymentMethod> paymentMethods = new ArrayList<>();
        paymentMethods.add(new PaymentMethod("mb_bank", "MB Bank", "0981 072 773", R.drawable.ic_mb_bank));
        paymentMethods.add(new PaymentMethod("momo", "MoMo", "0981 072 773", R.drawable.ic_momo));

        paymentMethodsAdapter = new PaymentMethodsAdapter(paymentMethods, new OnItemClickListener.OnPaymentMethodSelectedListener() {
            @Override
            public void onPaymentMethodSelected(PaymentMethod method) {

            }
        });
        rvPaymentMethods.setLayoutManager(new LinearLayoutManager(this));
        rvPaymentMethods.setAdapter(paymentMethodsAdapter);

        selectedPaymentMethod = paymentMethods.get(0);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());
        btnConfirmPayment.setOnClickListener(v -> processPayment());
    }

    private void loadData() {
        tvMovieTitle.setText("The Dune Universe");
        tvDateTime.setText("Thứ 4, 19/03/2025 | 15:45");
        tvRoom.setText("Phòng chiếu: 03");
        tvSeats.setText("Ghế: C4, C5, D7");

//        Glide.with(this)
//                .load(R.drawable.poster_dune)
//                .centerCrop()
//                .into(imgPoster);

        tvTicketPrice.setText("255.000đ");
        tvVat.setText("20.400đ");
        tvTotalPrice.setText("275.400đ");
    }


    private void processPayment() {
        Toast.makeText(this, "Đang xử lý thanh toán qua " + selectedPaymentMethod.getName(), Toast.LENGTH_SHORT).show();
    }
}