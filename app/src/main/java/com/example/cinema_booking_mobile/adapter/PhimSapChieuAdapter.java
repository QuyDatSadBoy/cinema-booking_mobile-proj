package com.example.cinema_booking_mobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinema_booking_mobile.R;
import com.example.cinema_booking_mobile.model.Phim;

import java.util.List;

public class PhimSapChieuAdapter extends RecyclerView.Adapter<PhimSapChieuAdapter.PhimSapChieuHolder> {
    private List<Phim> phimList;
    private OnItemClickListener.PhimSapChieuListener phimSapChieuListener;

    public PhimSapChieuAdapter(List<Phim> phimList) {
        this.phimList = phimList;
    }

    public void setPhimSapChieuListener(OnItemClickListener.PhimSapChieuListener listener) {
        this.phimSapChieuListener = listener;
    }

    @NonNull
    @Override
    public PhimSapChieuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new PhimSapChieuHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhimSapChieuHolder holder, int position) {
        Phim phim = phimList.get(position);
        holder.poster.setImageResource(Integer.parseInt(phim.getPoster()));

        View itemView = holder.itemView;
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) itemView.getLayoutParams();
        int paddingBonus = 125;
        if (position == 0) {
            params.setMarginStart(paddingBonus);
        }else if (position == phimList.size() - 1) {
            params.setMarginEnd(paddingBonus);
        }
        itemView.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return phimList.size();
    }

    public class PhimSapChieuHolder extends RecyclerView.ViewHolder {
        ImageView poster;

        public PhimSapChieuHolder(View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.poster);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (phimSapChieuListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            phimSapChieuListener.onPhimSapChieuClick(position);
                        }
                    }
                }
            });
        }
    }

}
