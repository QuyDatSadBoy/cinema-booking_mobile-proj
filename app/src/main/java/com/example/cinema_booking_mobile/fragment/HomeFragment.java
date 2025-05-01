package com.example.cinema_booking_mobile.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinema_booking_mobile.MainActivity;
import com.example.cinema_booking_mobile.R;
import com.example.cinema_booking_mobile.activity.MovieActivity;
import com.example.cinema_booking_mobile.adapter.OnItemClickListener;
import com.example.cinema_booking_mobile.model.Phim;
import com.example.cinema_booking_mobile.adapter.PhimDangChieuAdapter;
import com.example.cinema_booking_mobile.adapter.PhimSapChieuAdapter;
import com.example.cinema_booking_mobile.others.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements
        OnItemClickListener.PhimSapChieuListener,
        OnItemClickListener.PhimDangChieuListener {
    RecyclerView phimSapChieu, phimDangChieu;

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

        List<Phim> phimList = new ArrayList<>();
        Phim phim = new Phim(
                1,
                "Cuon theo chieu gio",
                "Lang man",
                120,
                "Tieng Anh",
                "No info",
                "no Info",
                "",
                String.valueOf(R.drawable.poster),
                "",
                2023,
                "No info",
                "No info",
                0f,
                "Sap chieu");
        for(int i = 0; i < 5; i++) {
            phimList.add(phim);
        }

        PhimSapChieuAdapter phimSapChieuAdapter = new PhimSapChieuAdapter(phimList);
        phimSapChieuAdapter.setPhimSapChieuListener(this);
        phimSapChieu.setAdapter(phimSapChieuAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        phimSapChieu.setLayoutManager(linearLayoutManager);

        PhimDangChieuAdapter phimDangChieuAdapter = new PhimDangChieuAdapter(phimList);
        phimDangChieuAdapter.setPhimDangChieuListener(this);
        phimDangChieu.setAdapter(phimDangChieuAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
//        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        phimDangChieu.setLayoutManager(gridLayoutManager);
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
    }

    @Override
    public void onPhimSapChieuClick(int position) {
        Intent intent = new Intent(getActivity(), MovieActivity.class);
        intent.putExtra("movieId", position);
        startActivity(intent);
    }

    @Override
    public void onPhimDangChieuClick(int position) {
        Intent intent = new Intent(getActivity(), MovieActivity.class);
        intent.putExtra("movieId", position);
        startActivity(intent);
    }
}
