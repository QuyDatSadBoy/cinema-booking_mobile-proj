package com.example.cinema_booking_mobile.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinema_booking_mobile.R;
import com.example.cinema_booking_mobile.adapter.DateAdapter;
import com.example.cinema_booking_mobile.adapter.OnItemClickListener;
import com.example.cinema_booking_mobile.adapter.TimeAdapter;
import com.example.cinema_booking_mobile.model.DateItem;
import com.example.cinema_booking_mobile.model.TimeItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class BookingTitketActivity extends AppCompatActivity {
    private DateAdapter dateAdapter;
    private TimeAdapter timeAdapter;

    private RecyclerView dateRecyclerView;
    private RecyclerView timeRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_ticket);


        dateRecyclerView = findViewById(R.id.dateRecyclerView);
        timeRecyclerView = findViewById(R.id.timeRecyclerView);

        setupDateRecyclerView();
        setupTimeRecyclerView();

        loadDateData();
        loadTimeData();
    }

    private void setupDateRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        dateRecyclerView.setLayoutManager(layoutManager);


        dateAdapter = new DateAdapter(new ArrayList<DateItem>(), new OnItemClickListener.DateItemClickListener() {
            @Override
            public void onDateItemClick(DateItem dateItem, int position) {
                loadTimeDataForDate(dateItem);
            }
        });

        dateRecyclerView.setAdapter(dateAdapter);
    }

    private void setupTimeRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        timeRecyclerView.setLayoutManager(layoutManager);

        timeAdapter = new TimeAdapter(new ArrayList<TimeItem>(), new OnItemClickListener.TimeItemClickListener() {
            @Override
            public void onTimeItemClick(TimeItem timeItem, int position) {
            }
        });

        timeRecyclerView.setAdapter(timeAdapter);
    }

    private void loadDateData() {
        List<DateItem> dateItems = generateNextDays(7);

        if (!dateItems.isEmpty()) {
            dateItems.get(1).setSelected(true);
        }

        dateAdapter.updateData(dateItems);

        dateRecyclerView.scrollToPosition(1);
    }

    private void loadTimeData() {
        List<TimeItem> timeItems = new ArrayList<>();
        timeItems.add(new TimeItem("13:00"));
        timeItems.add(new TimeItem("15:45", true, true));
        timeItems.add(new TimeItem("18:50"));
        timeItems.add(new TimeItem("20:30"));
        timeItems.add(new TimeItem("18:50"));
        timeItems.add(new TimeItem("20:30"));
        timeItems.add(new TimeItem("18:50"));
        timeItems.add(new TimeItem("20:30"));
        timeItems.add(new TimeItem("18:50"));
        timeItems.add(new TimeItem("20:30"));

        timeAdapter.updateData(timeItems);
    }

    private void loadTimeDataForDate(DateItem dateItem) {

        List<TimeItem> timeItems = new ArrayList<>();
        timeItems.add(new TimeItem("13:00", true, true));
        timeItems.add(new TimeItem("15:45"));
        timeItems.add(new TimeItem("18:50"));
        timeItems.add(new TimeItem("20:30"));

        timeAdapter.updateData(timeItems);
    }

    private List<DateItem> generateNextDays(int count) {
        List<DateItem> dateItems = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
        SimpleDateFormat dayNumberFormat = new SimpleDateFormat("d", Locale.getDefault());

        for (int i = 0; i < count; i++) {
            String dayName = dayNameFormat.format(calendar.getTime());
            String dayNumber = dayNumberFormat.format(calendar.getTime());

            dateItems.add(new DateItem(dayName, dayNumber));

            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        return dateItems;
    }
}
