package com.example.cinema_booking_mobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinema_booking_mobile.R;
import com.example.cinema_booking_mobile.model.Phim;

import java.util.ArrayList;
import java.util.List;

public class PhimTimKiemAdapter extends RecyclerView.Adapter<PhimTimKiemAdapter.PhimTimKiemHolder> {
    private List<Phim> phimList;
    private List<Phim> phimTimKiemList;
    private OnItemClickListener.PhimTimKiemListener phimTimKiemListener;

    public PhimTimKiemAdapter(List<Phim> phimList) {
        this.phimList = new ArrayList<>(phimList);
        this.phimTimKiemList = new ArrayList<>(phimList);
    }

    public void setPhimTimKiemListener(OnItemClickListener.PhimTimKiemListener phimTimKiemListener) {
        this.phimTimKiemListener = phimTimKiemListener;
    }

    public void filterList(String query) {
        phimTimKiemList.clear();
        for(Phim phim: phimList) {
            if(phim.getTen().toLowerCase().contains(query.toLowerCase())) {
                phimTimKiemList.add(phim);
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PhimTimKiemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item3, parent, false);
        return new PhimTimKiemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhimTimKiemHolder holder, int position) {
        Phim phim = phimTimKiemList.get(position);
        holder.poster.setImageResource(Integer.parseInt(phim.getPoster()));
        holder.tenPhim.setText(phim.getTen());
        holder.theLoai.setText("Thể loại: " + phim.getTheLoai());
        holder.doDai.setText("Thời lượng: " + phim.getDoDai() + " phút");
        holder.daoDien.setText("Đạo diễn: " + phim.getDaoDien());
        holder.trangThai.setText("Trạng thái: " + phim.getTrangThai());
        holder.doTuoi.setText(phim.getDoTuoi());
    }

    @Override
    public int getItemCount() {
        return phimTimKiemList.size();
    }

    public class PhimTimKiemHolder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView tenPhim, theLoai, doDai, daoDien, trangThai;
        Button doTuoi;

        public PhimTimKiemHolder(View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.poster);
            tenPhim = itemView.findViewById(R.id.tenPhim);
            theLoai = itemView.findViewById(R.id.theLoai);
            doDai = itemView.findViewById(R.id.doDai);
            daoDien = itemView.findViewById(R.id.daoDien);
            trangThai = itemView.findViewById(R.id.trangThai);
            doTuoi = itemView.findViewById(R.id.doTuoi);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (phimTimKiemListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            phimTimKiemListener.onPhimTimKiemClick(position);
                        }
                    }
                }
            });
        }
    }

}
