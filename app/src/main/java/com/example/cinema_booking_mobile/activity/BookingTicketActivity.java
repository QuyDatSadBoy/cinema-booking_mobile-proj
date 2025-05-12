package com.example.cinema_booking_mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinema_booking_mobile.R;
import com.example.cinema_booking_mobile.adapter.DateAdapter;
import com.example.cinema_booking_mobile.adapter.OnItemClickListener;
import com.example.cinema_booking_mobile.adapter.TimeAdapter;
import com.example.cinema_booking_mobile.dto.response.ChonGheResponse;
import com.example.cinema_booking_mobile.dto.response.GheDTO;
import com.example.cinema_booking_mobile.dto.response.GioChieuReponse;
import com.example.cinema_booking_mobile.dto.response.LichChieuDTO;
import com.example.cinema_booking_mobile.dto.response.SoDoGheResponse;
import com.example.cinema_booking_mobile.model.DateItem;
import com.example.cinema_booking_mobile.model.TimeItem;
import com.example.cinema_booking_mobile.service.ILichChieuService;
import com.example.cinema_booking_mobile.service.IPaymentService;
import com.example.cinema_booking_mobile.util.ApiUtils;
import com.example.cinema_booking_mobile.util.NetworkUtil;
import com.example.cinema_booking_mobile.util.SessionManager;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingTicketActivity extends AppCompatActivity {
    private DateAdapter dateAdapter;
    private TimeAdapter timeAdapter;

    private RecyclerView dateRecyclerView;
    private RecyclerView timeRecyclerView;

    private ILichChieuService lichChieuService;
    private IPaymentService iPaymentService;
    private SessionManager sessionManager;
    private Integer movieId;

    private Map<String, TextView> seatViews = new HashMap<>();
    private SoDoGheResponse currentSeatMap;
    private List<GheDTO> selectedSeats = new ArrayList<>();
    private TimeItem selectedTime;
    private DateItem selectedDate;
    private TextView titleText;
    private TextView tvTotalPrice;
    private Button btnBuy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_ticket);

        movieId = getIntent().getIntExtra("movieId", -1);
        if (movieId != -1) {
            System.out.println("Movie ID received: " + movieId);
        } else {
            System.out.println("No Movie ID received!");
        }

        movieId =2;

        lichChieuService = ApiUtils.getLichChieuService();
        iPaymentService = ApiUtils.getPaymentService();
        sessionManager = new SessionManager(this);
        dateRecyclerView = findViewById(R.id.dateRecyclerView);
        timeRecyclerView = findViewById(R.id.timeRecyclerView);

        titleText = findViewById(R.id.titleText);

        LinearLayout priceSection = findViewById(R.id.priceSection);
        tvTotalPrice = priceSection.getChildAt(0)
                .findViewById(R.id.totalPrice);
        btnBuy = findViewById(R.id.buyButton);

        setupSeatGrid();

        findViewById(R.id.backButton).setOnClickListener(v -> finish());
        setupRecyclerView();

        loadDateData();

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedSeats.isEmpty()) {
                    Toast.makeText(BookingTicketActivity.this, "Vui lòng chọn ghế", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Integer> lichChieuGheId = new ArrayList<>();
                for (GheDTO ghe: selectedSeats){
                    lichChieuGheId.add(ghe.getLichChieuGheId());
                }

                String token = sessionManager.getAuthorizationHeader();
                iPaymentService.luuTrangThaiGiuGhe(token, lichChieuGheId).enqueue(new Callback<ChonGheResponse>() {
                    @Override
                    public void onResponse(Call<ChonGheResponse> call, Response<ChonGheResponse> response) {
                        if (response.isSuccessful()) {
                            ChonGheResponse result = response.body();
                            System.out.println("Thành công: " + result);
                        } else {
                            System.out.println("Thất bại: " + response.errorBody());
                        }
                    }

                    @Override
                    public void onFailure(Call<ChonGheResponse> call, Throwable t) {
                        System.out.println("Lỗi: " + t.getMessage());
                    }
                });


                Intent intent = new Intent(BookingTicketActivity.this, PaymentDetailsActivity.class);
                intent.putExtra("movieId", movieId);
                intent.putExtra("movieName", currentSeatMap.getTenPhim());
                intent.putExtra("selectedSeats", (Serializable) selectedSeats);
                intent.putExtra("selectedTime", selectedTime);
                intent.putExtra("selectedDate", selectedDate);
                intent.putExtra("tenPhong", currentSeatMap.getTenPhong());
                intent.putExtra("giaVe", currentSeatMap.getGiaVe());
                intent.putExtra("poster", getIntent().getStringExtra("poster"));
                startActivity(intent);
            }
        });
    }


    private void onSeatClick(String seatName, TextView seatView) {
        if (currentSeatMap == null || currentSeatMap.getSoDoGhe() == null) {
            Log.e("onSeatClick", "CurrentSeatMap or SoDoGhe is null");
            return;
        }
        GheDTO targetSeat = null;
        Map<String, List<GheDTO>> soDoGhe = currentSeatMap.getSoDoGhe();

        for (Map.Entry<String, List<GheDTO>> entry : soDoGhe.entrySet()) {
            List<GheDTO> rowSeats = entry.getValue();

            if (rowSeats != null) {
                for (GheDTO seat : rowSeats) {
                    if (seatName.equals(seat.getTen())) {
                        targetSeat = seat;
                        break;
                    }
                }
            }
            if (targetSeat != null) break;
        }

        if (targetSeat == null) {
            Log.w("onSeatClick", "No seat found with name: " + seatName);
            return;
        }

        if ("Đã đặt".equals(targetSeat.getTrangThai())) {
            Toast.makeText(this, "Ghế này đã được đặt", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isCurrentlySelected = false;
        GheDTO seatToRemove = null;

        for (GheDTO selected : selectedSeats) {
            if (seatName.equals(selected.getTen())) {
                isCurrentlySelected = true;
                seatToRemove = selected;
                break;
            }
        }

        if (isCurrentlySelected) {
            selectedSeats.remove(seatToRemove);
            seatView.setTextAppearance(R.style.SeatStyle_Available);
            seatView.setBackgroundResource(R.drawable.seat_available);
        } else {
            if (selectedSeats.size() >= 8) {
                Toast.makeText(this, "Tối đa 8 ghế mỗi lần đặt", Toast.LENGTH_SHORT).show();
                return;
            }

            selectedSeats.add(targetSeat);
            seatView.setTextAppearance(R.style.SeatStyle_Selected);
            seatView.setBackgroundResource(R.drawable.seat_selected);
        }

        updatePriceInfo();
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        dateRecyclerView.setLayoutManager(layoutManager);


        dateAdapter = new DateAdapter(new ArrayList<DateItem>(), new OnItemClickListener.DateItemClickListener() {
            @Override
            public void onDateItemClick(DateItem dateItem, int position) {
                selectedDate = dateItem;
                selectedTime = null;
                updatePriceInfo();
                loadTimeData(movieId, dateItem.getDate());
            }
        });

        dateRecyclerView.setAdapter(dateAdapter);

        LinearLayoutManager layoutManagerTime = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        timeRecyclerView.setLayoutManager(layoutManagerTime);

        timeAdapter = new TimeAdapter(new ArrayList<TimeItem>(), new OnItemClickListener.TimeItemClickListener() {
            @Override
            public void onTimeItemClick(TimeItem timeItem, int position) {
                loadSeatMap(timeItem.getLichChieuId());
            }
        });

        timeRecyclerView.setAdapter(timeAdapter);
    }

    private void loadDateData() {
        List<DateItem> dateItems = generateNextDays(20);

        if (!dateItems.isEmpty()) {
            dateItems.get(4).setSelected(true);
        }
        selectedDate = dateItems.get(4);
        dateAdapter.updateData(dateItems);
        loadTimeData(movieId, dateItems.get(4).getDate());
        dateRecyclerView.scrollToPosition(1);
    }

    private void displaySeatMap(SoDoGheResponse seatMap) {

        titleText.setText(seatMap.getTenPhim());

        for (TextView seatView : seatViews.values()) {
            seatView.setBackgroundResource(R.drawable.seat_available);
            seatView.setTextAppearance(R.style.SeatStyle_Available);
            seatView.setTag(null);
            seatView.setEnabled(true);
        }

        if (seatMap == null || seatMap.getSoDoGhe() == null) {
            Log.e("updateSeatStates", "SeatMap or SoDoGhe is null");
            return;
        }

        Map<String, List<GheDTO>> soDoGhe = seatMap.getSoDoGhe();

        for (Map.Entry<String, List<GheDTO>> entry : soDoGhe.entrySet()) {
            List<GheDTO> rowSeats = entry.getValue();

            if (rowSeats != null) {
                for (GheDTO seat : rowSeats) {
                    TextView seatView = seatViews.get(seat.getTen());

                    if (seatView != null) {
                        seatView.setTag(seat);

                        if ("Đã đặt".equals(seat.getTrangThai()) || "Đã chọn".equals(seat.getTrangThai())) {
                            seatView.setBackgroundResource(R.drawable.seat_reserved);
                            seatView.setTextAppearance(R.style.SeatStyle_Reserved);
                            seatView.setEnabled(false);
                        } else {
                            seatView.setBackgroundResource(R.drawable.seat_available);
                            seatView.setTextAppearance(R.style.SeatStyle_Available);
                            seatView.setEnabled(true);
                        }
                    }
                }
            }
        }

    }

    private void loadTimeData(Integer phimId, LocalDate ngayChieu) {
        List<TimeItem> timeItems = new ArrayList<>();
        setupSeatGrid();
        if(NetworkUtil.isNetworkAvailable(this)){
            lichChieuService.getGioChieuByPhimIdAndNgayAndPhongId(phimId, ngayChieu).enqueue(new Callback<GioChieuReponse>() {
                @Override
                public void onResponse(Call<GioChieuReponse> call, Response<GioChieuReponse> response) {
                    timeItems.clear();
                    if(response.isSuccessful() && response.body().getMessage().equals("Lấy giờ chiếu thành công")){
                        GioChieuReponse gioChieuReponse = response.body();
                        int check = 0;

                        for(LichChieuDTO lichChieuDTO : gioChieuReponse.getData()){
                            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                            String gioBatDau = timeFormat.format(lichChieuDTO.getBatDau());
                            if(check == 0){
                                TimeItem timeItem = new TimeItem(gioBatDau, true, true, lichChieuDTO.getId());
                                timeItems.add(timeItem);

                                selectedTime = timeItem;
                                Log.d("loadTimeData", "Auto loading seat map for first time: " + lichChieuDTO.getId());
                                loadSeatMap(lichChieuDTO.getId());
                            }
                            else {
                                TimeItem timeItem = new TimeItem(gioBatDau, lichChieuDTO.getId());
                                timeItems.add(timeItem);
                            }
                            check++;
                        }
                        timeAdapter.updateData(timeItems);
                    }
                    else if(response.body().getMessage().equals("Không tìm thấy giờ chiếu")){
                        for (Map.Entry<String, TextView> entry : seatViews.entrySet()) {
                            TextView seatView = entry.getValue();
                            seatView.setBackgroundResource(R.drawable.seat_reserved);
                            seatView.setTextAppearance(R.style.SeatStyle_Reserved);
                            seatView.setEnabled(false);
                        }
                        timeAdapter.updateData(timeItems);
                    }
                    else {
                        Log.e("loadTimeData", "Response không thành công hoặc body null");
                    }
                }
                @Override
                public void onFailure(Call<GioChieuReponse> call, Throwable t) {
                    Log.e("loadTimeData", "API Error: " + t.getMessage());
                }
            });
        }
    }

    private void loadSeatMap(Integer lichChieuId) {
        if (!NetworkUtil.isNetworkAvailable(this)) {
            Toast.makeText(this, "Không có kết nối mạng", Toast.LENGTH_SHORT).show();
            return;
        }

        lichChieuService.getSoDoGheByLichChieuId(lichChieuId)
                .enqueue(new Callback<SoDoGheResponse>() {
                    @Override
                    public void onResponse(Call<SoDoGheResponse> call, Response<SoDoGheResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            currentSeatMap = response.body();
                            displaySeatMap(currentSeatMap);

                            selectedSeats.clear();
                            updatePriceInfo();


                        } else {
                            Toast.makeText(BookingTicketActivity.this,
                                    "Không thể tải sơ đồ ghế", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<SoDoGheResponse> call, Throwable t) {
                        Toast.makeText(BookingTicketActivity.this,
                                "Không thể kết nối đến server", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupSeatGrid() {
        seatViews.put("A1", findViewById(R.id.seat_a1));
        seatViews.put("A2", findViewById(R.id.seat_a2));
        seatViews.put("A3", findViewById(R.id.seat_a3));
        seatViews.put("A4", findViewById(R.id.seat_a4));
        seatViews.put("A5", findViewById(R.id.seat_a5));
        seatViews.put("A6", findViewById(R.id.seat_a6));
        seatViews.put("B1", findViewById(R.id.seat_b1));
        seatViews.put("B2", findViewById(R.id.seat_b2));
        seatViews.put("B3", findViewById(R.id.seat_b3));
        seatViews.put("B4", findViewById(R.id.seat_b4));
        seatViews.put("B5", findViewById(R.id.seat_b5));
        seatViews.put("B6", findViewById(R.id.seat_b6));
        seatViews.put("B7", findViewById(R.id.seat_b7));
        seatViews.put("B8", findViewById(R.id.seat_b8));
        seatViews.put("C1", findViewById(R.id.seat_c1));
        seatViews.put("C2", findViewById(R.id.seat_c2));
        seatViews.put("C3", findViewById(R.id.seat_c3));
        seatViews.put("C4", findViewById(R.id.seat_c4));
        seatViews.put("C5", findViewById(R.id.seat_c5));
        seatViews.put("C6", findViewById(R.id.seat_c6));
        seatViews.put("C7", findViewById(R.id.seat_c7));
        seatViews.put("C8", findViewById(R.id.seat_c8));
        seatViews.put("D1", findViewById(R.id.seat_d1));
        seatViews.put("D2", findViewById(R.id.seat_d2));
        seatViews.put("D3", findViewById(R.id.seat_d3));
        seatViews.put("D4", findViewById(R.id.seat_d4));
        seatViews.put("D5", findViewById(R.id.seat_d5));
        seatViews.put("D6", findViewById(R.id.seat_d6));
        seatViews.put("D7", findViewById(R.id.seat_d7));
        seatViews.put("D8", findViewById(R.id.seat_d8));
        seatViews.put("E1", findViewById(R.id.seat_e1));
        seatViews.put("E2", findViewById(R.id.seat_e2));
        seatViews.put("E3", findViewById(R.id.seat_e3));
        seatViews.put("E4", findViewById(R.id.seat_e4));
        seatViews.put("E5", findViewById(R.id.seat_e5));
        seatViews.put("E6", findViewById(R.id.seat_e6));
        seatViews.put("E7", findViewById(R.id.seat_e7));
        seatViews.put("E8", findViewById(R.id.seat_e8));
        seatViews.put("F1", findViewById(R.id.seat_f1));
        seatViews.put("F2", findViewById(R.id.seat_f2));
        seatViews.put("F3", findViewById(R.id.seat_f3));
        seatViews.put("F4", findViewById(R.id.seat_f4));
        seatViews.put("F5", findViewById(R.id.seat_f5));
        seatViews.put("F6", findViewById(R.id.seat_f6));
        for (Map.Entry<String, TextView> entry : seatViews.entrySet()) {
            String seatName = entry.getKey();
            TextView seatView = entry.getValue();

            if (seatView != null) {
                seatView.setOnClickListener(v -> onSeatClick(seatName, seatView));
            }
        }
    }

    private void updatePriceInfo() {
        if (selectedSeats.isEmpty() || currentSeatMap == null) {
            tvTotalPrice.setText("0d");
            btnBuy.setEnabled(false);
        } else {
            double totalPrice = 0;
            for (GheDTO seat : selectedSeats) {
                totalPrice += currentSeatMap.getGiaVe();

            }

            DecimalFormat formatter = new DecimalFormat("#,###");
            tvTotalPrice.setText(formatter.format(totalPrice) + "d");
            btnBuy.setEnabled(true);
        }
    }

    private List<DateItem> generateNextDays(int count) {
        List<DateItem> dateItems = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
        SimpleDateFormat dayNumberFormat = new SimpleDateFormat("d", Locale.getDefault());

        for (int i = 0; i < count; i++) {
            String dayName = dayNameFormat.format(calendar.getTime());
            String dayNumber = dayNumberFormat.format(calendar.getTime());

            dateItems.add(new DateItem(dayName, dayNumber, calendar.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()));

            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        return dateItems;
    }
}
