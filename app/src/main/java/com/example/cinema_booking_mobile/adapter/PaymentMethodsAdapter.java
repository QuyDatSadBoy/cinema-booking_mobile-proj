package com.example.cinema_booking_mobile.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.cinema_booking_mobile.R;
import com.example.cinema_booking_mobile.model.PaymentMethod;

import java.util.List;

public class PaymentMethodsAdapter extends RecyclerView.Adapter<PaymentMethodsAdapter.PaymentMethodViewHolder> {

    private List<PaymentMethod> paymentMethods;
    private int selectedPosition = 0;
    private OnItemClickListener.OnPaymentMethodSelectedListener listener;

    public PaymentMethodsAdapter(List<PaymentMethod> paymentMethods, OnItemClickListener.OnPaymentMethodSelectedListener listener) {
        this.paymentMethods = paymentMethods;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PaymentMethodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_payment_method, parent, false);
        return new PaymentMethodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentMethodViewHolder holder, int position) {
        PaymentMethod method = paymentMethods.get(position);

        holder.imgLogo.setImageResource(method.getLogoResId());
        holder.tvName.setText(method.getName());
        holder.tvPhoneNumber.setText(method.getPhoneNumber());
        holder.radioButton.setChecked(position == selectedPosition);

        holder.itemView.setOnClickListener(v -> {
            int previousSelected = selectedPosition;
            selectedPosition = holder.getAdapterPosition();

            // Cập nhật trạng thái radio button
            notifyItemChanged(previousSelected);
            notifyItemChanged(selectedPosition);

            // Thông báo cho listener
            listener.onPaymentMethodSelected(method);
        });
    }

    @Override
    public int getItemCount() {
        return paymentMethods.size();
    }

    static class PaymentMethodViewHolder extends RecyclerView.ViewHolder {
        ImageView imgLogo;
        TextView tvName;
        TextView tvPhoneNumber;
        RadioButton radioButton;

        public PaymentMethodViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvPaymentName);
            imgLogo = itemView.findViewById(R.id.imgPaymentIcon);
            tvPhoneNumber = itemView.findViewById(R.id.tvPaymentDetail);
            radioButton = itemView.findViewById(R.id.radioPayment);
        }
    }
}