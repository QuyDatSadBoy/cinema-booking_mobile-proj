package com.example.cinema_booking_mobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cinema_booking_mobile.R;
import com.example.cinema_booking_mobile.model.TimeItem;
import java.util.ArrayList;
import java.util.List;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.ViewHolder> {

    private List<TimeItem> items;
    private OnItemClickListener.TimeItemClickListener onItemClickListener;

    public TimeAdapter(List<TimeItem> items, OnItemClickListener.TimeItemClickListener onItemClickListener) {
        this.items = items != null ? items : new ArrayList<>();
        this.onItemClickListener = onItemClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTime;

        public ViewHolder(View view) {
            super(view);
            tvTime = view.findViewById(R.id.tvTime);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_time, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final TimeItem item = items.get(position);
        holder.tvTime.setText(item.getTime());
        holder.tvTime.setSelected(item.isSelected());
        holder.tvTime.setAlpha(item.isAvailable() ? 1.0f : 0.5f);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int adapterPosition = holder.getAdapterPosition();
                if (item.isAvailable() && adapterPosition != RecyclerView.NO_POSITION) {
                    updateSelection(adapterPosition);
                    if (onItemClickListener != null) {
                        onItemClickListener.onTimeItemClick(item, adapterPosition);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateData(List<TimeItem> newItems) {
        this.items = newItems != null ? newItems : new ArrayList<>();
        notifyDataSetChanged();
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