package com.example.cinema_booking_mobile.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.InputStream;
import java.net.URL;
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

    public void updateData(List<Phim> newItems) {
        this.phimList = newItems != null ? newItems : new ArrayList<>();
        this.phimTimKiemList = newItems != null ? newItems : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void filterList(String query) {
        phimTimKiemList = new ArrayList<>();
        for(Phim phim: phimList) {
            if(phim.getTen().toLowerCase().contains(query.toLowerCase())) {
                phimTimKiemList.add(phim);
            }
        }
        notifyDataSetChanged();
    }

    public int getCurrentPhimId(int position) {
        return phimTimKiemList.get(position).getId();
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

        new Thread(() -> {
            try {
                InputStream in = new URL(phim.getPoster() + "lazy=load").openStream();
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                holder.poster.post(() -> holder.poster.setImageBitmap(bitmap));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        holder.ten.setText(phim.getTen());
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
        TextView ten, theLoai, doDai, daoDien, trangThai;
        Button doTuoi;

        public PhimTimKiemHolder(View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.poster);
            ten = itemView.findViewById(R.id.ten);
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
