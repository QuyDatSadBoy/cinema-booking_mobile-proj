package com.example.cinema_booking_mobile.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinema_booking_mobile.R;
import com.example.cinema_booking_mobile.activity.MovieActivity;
import com.example.cinema_booking_mobile.adapter.OnItemClickListener;
import com.example.cinema_booking_mobile.dto.response.PhimDTO;
import com.example.cinema_booking_mobile.model.Phim;
import com.example.cinema_booking_mobile.adapter.PhimDangChieuAdapter;
import com.example.cinema_booking_mobile.adapter.PhimSapChieuAdapter;
import com.example.cinema_booking_mobile.others.SpaceItemDecoration;
import com.example.cinema_booking_mobile.service.IPhimService;
import com.example.cinema_booking_mobile.util.ApiUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements
        OnItemClickListener.PhimSapChieuListener,
        OnItemClickListener.PhimDangChieuListener {
    RecyclerView phimSapChieu, phimDangChieu;
    PhimSapChieuAdapter phimSapChieuAdapter;
    PhimDangChieuAdapter phimDangChieuAdapter;
    private IPhimService iPhimService;
    private List<PhimDTO> dsPhim = new ArrayList<>();
    List<Phim> dsPhimDangChieu = new ArrayList<>();
    List<Phim> dsPhimSapChieu = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        phimSapChieu = view.findViewById(R.id.phimSapChieu);
        phimDangChieu = view.findViewById(R.id.phimDangChieu);

        phimSapChieuAdapter = new PhimSapChieuAdapter(dsPhimSapChieu);
        phimSapChieuAdapter.setPhimSapChieuListener(this);
        phimSapChieu.setAdapter(phimSapChieuAdapter);
        phimSapChieu.setLayoutManager(new LinearLayoutManager(
                getActivity(), LinearLayoutManager.HORIZONTAL, false));

        phimDangChieuAdapter = new PhimDangChieuAdapter(dsPhimDangChieu);
        phimDangChieuAdapter.setPhimDangChieuListener(this);
        phimDangChieu.setAdapter(phimDangChieuAdapter);
        phimDangChieu.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        phimDangChieu.addItemDecoration(new SpaceItemDecoration(35));

        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(phimSapChieu);
        phimSapChieu.smoothScrollToPosition(1);
        phimSapChieu.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int centerX = recyclerView.getWidth() / 2;
                for (int i = 0; i < recyclerView.getChildCount(); i++) {
                    View child = recyclerView.getChildAt(i);
                    float childCenterX = (child.getLeft() + child.getRight()) / 2f;
                    float distance = Math.abs(centerX - childCenterX);

                    float maxScale = 1.0f;
                    float minScale = 0.66f;
                    float maxDistance = recyclerView.getWidth() / 2f;

                    float scale = maxScale - (distance / maxDistance) * (maxScale - minScale);
                    scale = Math.max(minScale, Math.min(maxScale, scale));

                    child.setScaleX(scale);
                    child.setScaleY(scale);
                }
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int centerX = recyclerView.getWidth() / 2;
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    for (int i = 0; i < recyclerView.getChildCount(); i++) {
                        View child = recyclerView.getChildAt(i);
                        float childCenterX = (child.getLeft() + child.getRight()) / 2f;
                        float distance = Math.abs(centerX - childCenterX);

                        float maxScale = 1.0f;
                        float minScale = 0.66f;
                        float maxDistance = recyclerView.getWidth() / 2f;

                        float scale = maxScale - (distance / maxDistance) * (maxScale - minScale);
                        scale = Math.max(minScale, Math.min(maxScale, scale));

                        child.animate()
                                .scaleX(scale)
                                .scaleY(scale)
                                .setDuration(200)
                                .setInterpolator(new DecelerateInterpolator())
                                .start();
                    }
                }
            }
        });

        iPhimService = ApiUtils.getPhimService();
        iPhimService.getAllPhim().enqueue(new Callback<List<PhimDTO>>() {
            @Override
            public void onResponse(Call<List<PhimDTO>> call, Response<List<PhimDTO>> response) {
                if (response.isSuccessful()) {
                    dsPhim = response.body();
                    dsPhimDangChieu = dsPhim.stream().map(phimDTO ->
                                    new Phim(
                                            phimDTO.getId(), phimDTO.getTen(), phimDTO.getTheLoai(), phimDTO.getDoDai(),
                                            phimDTO.getNgonNgu(), phimDTO.getDaoDien(),phimDTO.getDienVien(),
                                            phimDTO.getMoTa(), phimDTO.getPoster(), phimDTO.getTrailer(),
                                            phimDTO.getNamSx(), phimDTO.getHangSx(), phimDTO.getDoTuoi(),
                                            phimDTO.getDanhGia(), phimDTO.getTrangThai()))
                            .filter(phim -> phim.getTrangThai().equalsIgnoreCase("Đang chiếu"))
                            .collect(Collectors.toList());
                    dsPhimSapChieu = dsPhim.stream().map(phimDTO ->
                                    new Phim(
                                            phimDTO.getId(), phimDTO.getTen(), phimDTO.getTheLoai(), phimDTO.getDoDai(),
                                            phimDTO.getNgonNgu(), phimDTO.getDaoDien(),phimDTO.getDienVien(),
                                            phimDTO.getMoTa(), phimDTO.getPoster(), phimDTO.getTrailer(),
                                            phimDTO.getNamSx(), phimDTO.getHangSx(), phimDTO.getDoTuoi(),
                                            phimDTO.getDanhGia(), phimDTO.getTrangThai()))
                            .filter(phim -> phim.getTrangThai().equalsIgnoreCase("Sắp chiếu"))
                            .collect(Collectors.toList());

                    phimDangChieuAdapter.updateData(dsPhimDangChieu);
                    phimSapChieuAdapter.updateData(dsPhimSapChieu);

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
    public void onPhimSapChieuClick(int position) {
        Intent intent = new Intent(getActivity(), MovieActivity.class);
        intent.putExtra("movieId", dsPhimSapChieu.get(position).getId());
        startActivity(intent);
    }

    @Override
    public void onPhimDangChieuClick(int position) {
        Intent intent = new Intent(getActivity(), MovieActivity.class);
        intent.putExtra("movieId", dsPhimDangChieu.get(position).getId());
        startActivity(intent);
    }
}
