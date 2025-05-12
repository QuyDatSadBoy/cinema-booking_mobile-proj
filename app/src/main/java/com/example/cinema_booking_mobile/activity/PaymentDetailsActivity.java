package com.example.cinema_booking_mobile.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cinema_booking_mobile.R;
import com.example.cinema_booking_mobile.adapter.OnItemClickListener;
import com.example.cinema_booking_mobile.adapter.PaymentMethodsAdapter;
import com.example.cinema_booking_mobile.dto.request.ThanhToanRequest;
import com.example.cinema_booking_mobile.dto.response.GheDTO;
import com.example.cinema_booking_mobile.dto.response.PaymentMethodDTO;
import com.example.cinema_booking_mobile.dto.response.ThanhToanResponse;
import com.example.cinema_booking_mobile.model.DateItem;
import com.example.cinema_booking_mobile.model.PaymentMethod;
import com.example.cinema_booking_mobile.model.TimeItem;
import com.example.cinema_booking_mobile.service.IPaymentService;
import com.example.cinema_booking_mobile.util.ApiUtils;
import com.example.cinema_booking_mobile.util.NetworkUtil;
import com.example.cinema_booking_mobile.util.SessionManager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentDetailsActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private ImageView imgPoster;
    private TextView tvMovieTitle, tvDateTime, tvRoom, tvSeats;
    private TextView tvTicketPrice, tvVat, tvTotalPrice;
    private RecyclerView rvPaymentMethods;
    private Button btnConfirmPayment;

    private PaymentMethodsAdapter paymentMethodsAdapter;
    private PaymentMethod selectedPaymentMethod;
    private IPaymentService iPaymentService;
    private SessionManager sessionManager;

    private List<GheDTO> selectedSeats = new ArrayList<>();
    private Integer movieId;
    private DateItem selectedDate;
    private TimeItem selectedTime;
    private String movieName;
    private String tenPhong;
    private Double giaVe;
    private Double tongTien;
    private String poster;

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
        iPaymentService = ApiUtils.getPaymentService();
        sessionManager = new SessionManager(this);
    }

    private void setupPaymentMethodsAdapter() {
        List<PaymentMethod> paymentMethods = new ArrayList<>();

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
                                        paymentMethodDTO.getTen().equals("Momo") ?  R.drawable.ic_momo :R.drawable.ic_mb_bank

                                ));

                                setupPaymentMethodsAdapter(paymentMethods);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<PaymentMethodDTO>> call, Throwable t) {

                    }
                }
        );

    }

    private void setupPaymentMethodsAdapter(List<PaymentMethod> paymentMethods) {
        paymentMethodsAdapter = new PaymentMethodsAdapter(paymentMethods, new OnItemClickListener.OnPaymentMethodSelectedListener() {
            @Override
            public void onPaymentMethodSelected(PaymentMethod method) {
                selectedPaymentMethod = method;
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

    @SuppressLint("SetTextI18n")
    private void loadData() {
        movieId = getIntent().getIntExtra("movieId", -1);
        selectedTime = (TimeItem) getIntent().getSerializableExtra("selectedTime");
        selectedDate = (DateItem) getIntent().getSerializableExtra("selectedDate");
        tenPhong = getIntent().getStringExtra("tenPhong");
        selectedSeats = (ArrayList<GheDTO>) getIntent().getSerializableExtra("selectedSeats");
        movieName = getIntent().getStringExtra("movieName");
        giaVe =getIntent().getDoubleExtra("giaVe", 0);
        poster = getIntent().getStringExtra("poster");

        if (poster != null && !poster.isEmpty()) {
            Glide.with(this)
                    .load("https://th.bing.com/th/id/R.24f6a65f3371b06c7ae158bfad7f8151?rik=87qYKCQY3eiDCQ&pid=ImgRaw&r=0")
                    .placeholder(R.drawable.img)
                    .error(R.drawable.img)
                    .centerCrop()
                    .into(imgPoster);
        }

        tvMovieTitle.setText(movieName);
        tvDateTime.setText(" "
                + selectedDate.getDayName()
                + " , "
                + selectedDate.getDate().toString()
                + " | "
                + selectedTime.getTime()
                +" ");
        tvRoom.setText("Phòng chiếu: "+ tenPhong);

        String ghe = "Ghế: ";
        for(GheDTO x: selectedSeats) {
            ghe += x.getTen() + ", ";
        }
        tvSeats.setText(ghe.substring(0, ghe.length()-2));
        double totalPrice = giaVe * selectedSeats.size();
        DecimalFormat formatter = new DecimalFormat("#,###");
        tvTicketPrice.setText(formatter.format(totalPrice) + "đ");
        double VAT = 0.08 * totalPrice;
        tvVat.setText(formatter.format(VAT) + "d");

        tongTien = VAT + totalPrice;
        tvTotalPrice.setText(formatter.format(tongTien) + "đ");
    }

    private void processPayment() {
        List<Integer> lichChieuGheId = new ArrayList<>();
        for (GheDTO ghe: selectedSeats){
            lichChieuGheId.add(ghe.getLichChieuGheId());
        }

        ThanhToanRequest thanhToanRequest = new ThanhToanRequest(lichChieuGheId, selectedPaymentMethod.getId(), tongTien);

        if (!NetworkUtil.isNetworkAvailable(this)) {
            Toast.makeText(this, "Không có kết nối mạng", Toast.LENGTH_SHORT).show();
            return;
        }

        iPaymentService.thanhToan(sessionManager.getAuthorizationHeader(), thanhToanRequest)
                .enqueue(new Callback<ThanhToanResponse>() {
                    @Override
                    public void onResponse(Call<ThanhToanResponse> call, Response<ThanhToanResponse> response) {
                        if(response.isSuccessful() && response.body()!=null){
                            new AlertDialog.Builder(PaymentDetailsActivity.this)
                                    .setTitle("Thanh toán thành công")
                                    .setMessage("Cảm ơn bạn đã thanh toán. Vé của bạn đã được xác nhận.")
                                    .setIcon(R.drawable.ic_calendar)
                                    .setPositiveButton("Xem vé", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(PaymentDetailsActivity.this, PersonalInfoActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    })
                                    .setCancelable(false)
                                    .show();
                        } else {
                            new AlertDialog.Builder(PaymentDetailsActivity.this)
                                    .setTitle("Thanh toán thất bại")
                                    .setMessage("Đã có lỗi xảy ra. Vui lòng thử lại.")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ThanhToanResponse> call, Throwable t) {
                        new AlertDialog.Builder(PaymentDetailsActivity.this)
                                .setTitle("Lỗi kết nối")
                                .setMessage("Không thể kết nối đến máy chủ. Vui lòng kiểm tra kết nối mạng và thử lại.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    }
                });
    }
}