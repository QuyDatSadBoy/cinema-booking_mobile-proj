package com.example.cinema_booking_mobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cinema_booking_mobile.R;
import com.example.cinema_booking_mobile.model.DateItem;
import java.util.ArrayList;
import java.util.List;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.ViewHolder> {

    private List<DateItem> items;
    private OnItemClickListener.DateItemClickListener onItemClickListener;

    public DateAdapter(List<DateItem> items, OnItemClickListener.DateItemClickListener onItemClickListener) {
        this.items = items != null ? items : new ArrayList<>();
        this.onItemClickListener = onItemClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDayName;
        public TextView tvDayNumber;
        public LinearLayout day;

        public ViewHolder(View view) {
            super(view);
            tvDayName = view.findViewById(R.id.tvDayName);
            tvDayNumber = view.findViewById(R.id.tvDayNumber);
            day = view.findViewById(R.id.day);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_date, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final DateItem item = items.get(position);
        holder.tvDayName.setText(item.getDayName());
        holder.tvDayNumber.setText(item.getDayNumber());
        holder.tvDayName.setSelected(item.isSelected());
        holder.tvDayNumber.setSelected(item.isSelected());
        holder.day.setSelected(item.isSelected());
        holder.tvDayName.setAlpha(item.isAvailable() ? 1.0f : 0.5f);
        holder.tvDayNumber.setAlpha(item.isAvailable() ? 1.0f : 0.5f);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int adapterPosition = holder.getAdapterPosition();
                if (item.isAvailable() && adapterPosition != RecyclerView.NO_POSITION) {
                    updateSelection(adapterPosition);
                    if (onItemClickListener != null) {
                        onItemClickListener.onDateItemClick(item, adapterPosition);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateData(List<DateItem> newItems) {
        this.items = newItems != null ? newItems : new ArrayList<>();
        notifyDataSetChanged();
    }

    public List<DateItem> getItems(){
        return this.items;
    }

    public void updateSelection(int position) {
        for (int i = 0; i < items.size(); i++) {
            boolean selected = i == position;
            if (items.get(i).isSelected() != selected) {
                items.get(i).setSelected(selected);
                notifyItemChanged(i);
            }
        }
    }
}