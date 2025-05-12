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
import com.example.cinema_booking_mobile.model.Ticket;

import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.ViewHolder> {

    private List<Ticket> ticketList;

    public TicketAdapter(List<Ticket> ticketList) {
        this.ticketList = ticketList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ticket, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ticket ticket = ticketList.get(position);

        holder.tvMovieTitle.setText(ticket.getMovieTitle());
        holder.btnStatus.setText(ticket.getStatus());
        holder.tvCinema.setText("Rạp: " + ticket.getCinema());
        holder.tvDate.setText("Ngày: " + ticket.getDate());
        holder.tvSeat.setText("Ghế: " + ticket.getSeatNumbers());

        if (ticket.getStatus().equals("Đã thanh toán")) {
            holder.btnStatus.setBackgroundResource(R.drawable.bg_age);
        } else {
            holder.btnStatus.setBackgroundResource(R.drawable.bg_status_canceled);
        }
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgMovie;
        TextView tvMovieTitle, tvCinema, tvDate, tvSeat;
        Button btnStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMovie = itemView.findViewById(R.id.imgMovie);
            tvMovieTitle = itemView.findViewById(R.id.tvMovieTitle);
            btnStatus = itemView.findViewById(R.id.btnStatus);
            tvCinema = itemView.findViewById(R.id.tvCinema);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvSeat = itemView.findViewById(R.id.tvSeat);
        }
    }
}