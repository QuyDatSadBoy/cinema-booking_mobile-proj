package com.example.cinema_booking_mobile.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinema_booking_mobile.R;
import com.example.cinema_booking_mobile.activity.MovieActivity;
import com.example.cinema_booking_mobile.adapter.OnItemClickListener;
import com.example.cinema_booking_mobile.dto.response.PhimDTO;
import com.example.cinema_booking_mobile.model.Phim;
import com.example.cinema_booking_mobile.adapter.PhimTimKiemAdapter;
import com.example.cinema_booking_mobile.service.IPhimService;
import com.example.cinema_booking_mobile.util.ApiUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment implements OnItemClickListener.PhimTimKiemListener {
    SearchView search;
    RecyclerView phimTimKiem;
    PhimTimKiemAdapter phimTimKiemAdapter;
    private IPhimService iPhimService;
    private List<Phim> dsPhim = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        search = view.findViewById(R.id.search);
        phimTimKiem = view.findViewById(R.id.phimTimKiem);

        phimTimKiemAdapter = new PhimTimKiemAdapter(dsPhim);
        phimTimKiemAdapter.setPhimTimKiemListener(this);
        phimTimKiem.setAdapter(phimTimKiemAdapter);
        phimTimKiem.setLayoutManager(new LinearLayoutManager(
                getActivity(), LinearLayoutManager.VERTICAL, false));

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

        iPhimService = ApiUtils.getPhimService();
        iPhimService.getAllPhim().enqueue(new Callback<List<PhimDTO>>() {
            @Override
            public void onResponse(Call<List<PhimDTO>> call, Response<List<PhimDTO>> response) {
                if (response.isSuccessful()) {
                    dsPhim = response.body()
                            .stream()
                            .map(phimDTO ->
                                    new Phim(
                                            phimDTO.getId(), phimDTO.getTen(), phimDTO.getTheLoai(), phimDTO.getDoDai(),
                                            phimDTO.getNgonNgu(), phimDTO.getDaoDien(),phimDTO.getDienVien(),
                                            phimDTO.getMoTa(), phimDTO.getPoster(), phimDTO.getTrailer(),
                                            phimDTO.getNamSx(), phimDTO.getHangSx(), phimDTO.getDoTuoi(),
                                            phimDTO.getDanhGia(), phimDTO.getTrangThai()))
                            .collect(Collectors.toList());

                    phimTimKiemAdapter.updateData(dsPhim);

                    System.out.println("Thành công: ");
                } else {
                    System.out.println("Thất bại: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<PhimDTO>> call, Throwable t) {
                System.out.println("Lỗi: " + t.getMessage());
            }
        });
    }

    @Override
    public void onPhimTimKiemClick(int position) {
        Intent intent = new Intent(getActivity(), MovieActivity.class);
        intent.putExtra("movieId", phimTimKiemAdapter.getCurrentPhimId(position));
        startActivity(intent);
    }
}
