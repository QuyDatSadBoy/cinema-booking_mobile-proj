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

public class PaymentMethodAdapter extends RecyclerView.Adapter<PaymentMethodAdapter.ViewHolder> {

    private List<PaymentMethod> paymentMethods;
    private OnPaymentMethodClickListener listener;
    private int selectedPosition = -1;

    public interface OnPaymentMethodClickListener {
        void onPaymentMethodClick(PaymentMethod paymentMethod, int position);
    }

    public PaymentMethodAdapter(List<PaymentMethod> paymentMethods, OnPaymentMethodClickListener listener) {
        this.paymentMethods = paymentMethods;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment_method, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PaymentMethod paymentMethod = paymentMethods.get(position);

        holder.tvPaymentName.setText(paymentMethod.getName());
        holder.imgPaymentIcon.setImageResource(paymentMethod.getLogoResId());
        holder.tvPaymentDetail.setText(paymentMethod.getPhoneNumber());
        holder.radioButton.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(v -> {
            int prevSelectedPos = selectedPosition;
            selectedPosition = holder.getAdapterPosition();

            notifyItemChanged(prevSelectedPos);
            notifyItemChanged(selectedPosition);

            if (listener != null) {
                listener.onPaymentMethodClick(paymentMethod, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return paymentMethods.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPaymentIcon;
        TextView tvPaymentName, tvPaymentDetail;
        RadioButton radioButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPaymentIcon = itemView.findViewById(R.id.imgPaymentIcon);
            tvPaymentName = itemView.findViewById(R.id.tvPaymentName);
            tvPaymentDetail = itemView.findViewById(R.id.tvPaymentDetail);
            radioButton = itemView.findViewById(R.id.radioPayment);
        }
    }
    public PaymentMethod getSelectedPaymentMethod() {
        if (selectedPosition != -1 && selectedPosition < paymentMethods.size()) {
            return paymentMethods.get(selectedPosition);
        }
        return null;
    }
}