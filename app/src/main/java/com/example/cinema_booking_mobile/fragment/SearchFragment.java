package com.example.cinema_booking_mobile.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinema_booking_mobile.R;
import com.example.cinema_booking_mobile.model.Phim;
import com.example.cinema_booking_mobile.model.adapter.PhimTimKiemAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements PhimTimKiemAdapter.PhimTimKiemListener {
    SearchView search;
    RecyclerView phimTimKiem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_screen, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        search = view.findViewById(R.id.search);
        phimTimKiem = view.findViewById(R.id.phimTimKiem);

        List<Phim> phimList = new ArrayList<>();
        phimList.add(new Phim(1, "Cuon theo chieu gio", "Lang man", 120, "Tieng Anh", "No info", "", String.valueOf(R.drawable.poster), "", 2023, "No info", "13+", 0f, "Sap chieu"));
        phimList.add(new Phim(2, "Vu tru bao la", "Khoa hoc vien tuong", 140, "Tieng Viet", "No info", "", String.valueOf(R.drawable.poster), "", 2022, "No info", "16+", 0f, "Dang chieu"));
        phimList.add(new Phim(3, "Hanh trinh kham pha", "Phieu luu", 110, "Tieng Phap", "No info", "", String.valueOf(R.drawable.poster), "", 2021, "No info", "13+", 0f, "Sap chieu"));
        phimList.add(new Phim(4, "Tinh yeu va chien tranh", "Chien tranh", 150, "Tieng Anh", "No info", "", String.valueOf(R.drawable.poster), "", 2020, "No info", "18+", 0f, "Ngung chieu"));
        phimList.add(new Phim(5, "Am anh toi toi", "Kinh di", 95, "Tieng Han", "No info", "", String.valueOf(R.drawable.poster), "", 2023, "No info", "18+", 0f, "Dang chieu"));
        phimList.add(new Phim(6, "Ngay tro ve", "Tam ly", 125, "Tieng Nhat", "No info", "", String.valueOf(R.drawable.poster), "", 2019, "No info", "13+", 0f, "Sap chieu"));
        phimList.add(new Phim(7, "Thanh pho ngu quen", "Bi an", 105, "Tieng Viet", "No info", "", String.valueOf(R.drawable.poster), "", 2024, "No info", "16+", 0f, "Dang chieu"));
        phimList.add(new Phim(8, "Ky uc tuoi tho", "Gia dinh", 90, "Tieng Anh", "No info", "", String.valueOf(R.drawable.poster), "", 2022, "No info", "0+", 0f, "Sap chieu"));
        phimList.add(new Phim(9, "Nang mua ha", "Lang man", 115, "Tieng Trung", "No info", "", String.valueOf(R.drawable.poster), "", 2021, "No info", "13+", 0f, "Ngung chieu"));
        phimList.add(new Phim(10, "Vu dieu dem he", "Am nhac", 100, "Tieng Viet", "No info", "", String.valueOf(R.drawable.poster), "", 2023, "No info", "13+", 0f, "Dang chieu"));

        PhimTimKiemAdapter phimTimKiemAdapter = new PhimTimKiemAdapter(phimList);
        phimTimKiemAdapter.setPhimTimKiemListener(this);
        phimTimKiem.setAdapter(phimTimKiemAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        phimTimKiem.setLayoutManager(linearLayoutManager);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                phimTimKiemAdapter.filterList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                phimTimKiemAdapter.filterList(newText);
                return false;
            }
        });
    }

    @Override
    public void onPhimTimKiemClick(int position) {
        Toast.makeText(getActivity(),"Chi tiet phim tim kiem " + position, Toast.LENGTH_SHORT).show();
        // Open film details screen
    }
}
