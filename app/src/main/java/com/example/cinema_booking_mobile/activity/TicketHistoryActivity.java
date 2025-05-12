package com.example.cinema_booking_mobile.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinema_booking_mobile.R;
import com.example.cinema_booking_mobile.adapter.TicketAdapter;
import com.example.cinema_booking_mobile.model.Ticket;
import com.example.cinema_booking_mobile.service.ITicketHistoryService;
import com.example.cinema_booking_mobile.util.ApiClient;
import com.example.cinema_booking_mobile.util.ApiUtils;
import com.example.cinema_booking_mobile.util.DateFormatter;
import com.example.cinema_booking_mobile.util.SessionManager;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerViewTickets;
    private TabLayout tabLayout;
    private SearchView searchView;
    private ImageButton btnBack;
    private TicketAdapter ticketAdapter;
    private List<Ticket> ticketList;

    private SessionManager sessionManager;

    private ITicketHistoryService iTicketHistoryService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_history);

        iTicketHistoryService = ApiUtils.getTicketHistoryService();
        sessionManager = new SessionManager(this);

        recyclerViewTickets = findViewById(R.id.recyclerViewTickets);
        tabLayout = findViewById(R.id.tabLayout);
        searchView = findViewById(R.id.search);
        btnBack = findViewById(R.id.btnBack);

        ticketList = new ArrayList<>();

        recyclerViewTickets.setLayoutManager(new LinearLayoutManager(this));
        ticketAdapter = new TicketAdapter(ticketList);
        recyclerViewTickets.setAdapter(ticketAdapter);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        loadPaidTickets();
                        break;
                    case 1:
                        loadCancelledTickets();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Xử lý khi người dùng nhấn Enter sau khi nhập query
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Xử lý khi người dùng thay đổi text trong SearchView
                return true;
            }
        });

        loadPaidTickets();
    }

    private void loadPaidTickets() {
        String token = sessionManager.getAuthorizationHeader();
        iTicketHistoryService.getAllTickets(token).enqueue(new Callback<List<Ticket>>() {
            @Override
            public void onResponse(Call<List<Ticket>> call, Response<List<Ticket>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ticketList.clear();
                    List<Ticket> responseList = response.body();
                    for (Ticket ticket : responseList) {
                        String dateStr = ticket.getDate();
                        if (dateStr != null) {
                            String formattedDate = DateFormatter.apiDateTimeToMMDDYYYY(dateStr);
                            ticket.setDate(formattedDate);
                        }

                        String status = ticket.getStatus();
                        if (status != null && status.equals("Đã thanh toán")) {
                            ticketList.add(ticket);
                        }
                    }
                    ticketAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(TicketHistoryActivity.this,
                            "Không thể tải danh sách vé đã thanh toán",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Ticket>> call, Throwable t) {
                Toast.makeText(TicketHistoryActivity.this,
                        "Lỗi kết nối: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadCancelledTickets() {
        ticketList.clear();
        String token = sessionManager.getAuthorizationHeader();
        iTicketHistoryService.getAllTickets(token).enqueue(new Callback<List<Ticket>>() {
            @Override
            public void onResponse(Call<List<Ticket>> call, Response<List<Ticket>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ticketList.clear();
                    List<Ticket> responseList = response.body();
                    for (Ticket ticket : responseList) {
                        String dateStr = ticket.getDate();
                        if (dateStr != null) {
                            String formattedDate = DateFormatter.apiDateTimeToMMDDYYYY(dateStr);
                            ticket.setDate(formattedDate);
                        }

                        String status = ticket.getStatus();
                        if (status != null && status.equals("Đã huỷ")) {
                            ticketList.add(ticket);
                        }
                    }
                    ticketAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(TicketHistoryActivity.this,
                            "Không thể tải danh sách vé đã thanh toán",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Ticket>> call, Throwable t) {
                Toast.makeText(TicketHistoryActivity.this,
                        "Lỗi kết nối: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        ticketAdapter.notifyDataSetChanged();
    }
}