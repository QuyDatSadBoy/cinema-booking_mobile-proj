package com.example.cinema_booking_mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cinema_booking_mobile.R;
import com.example.cinema_booking_mobile.model.PerplexityModel;

import java.util.List;

public class ModelSpinnerAdapter extends ArrayAdapter<PerplexityModel> {
    private LayoutInflater inflater;

    public ModelSpinnerAdapter(Context context, List<PerplexityModel> models) {
        super(context, 0, models);
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomDropDownView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_model_spinner, parent, false);
        }

        TextView tvModelName = convertView.findViewById(R.id.tvModelName);
        PerplexityModel model = getItem(position);

        if (model != null) {
            tvModelName.setText(model.getName());
        }

        return convertView;
    }

    private View getCustomDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_model_dropdown, parent, false);
        }

        TextView tvModelName = convertView.findViewById(R.id.tvModelName);
        TextView tvModelCategory = convertView.findViewById(R.id.tvModelCategory);
        TextView tvModelDescription = convertView.findViewById(R.id.tvModelDescription);

        PerplexityModel model = getItem(position);

        if (model != null) {
            tvModelName.setText(model.getName());
            tvModelCategory.setText(model.getCategory());
            tvModelDescription.setText(model.getDescription());
        }

        return convertView;
    }
}