package com.example.mvvm_klimov.presentations;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvm_klimov.R;
import com.example.mvvm_klimov.databinding.ItemDayBinding;
import com.example.mvvm_klimov.domains.models.Day;

import java.util.ArrayList;
import java.util.List;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder> {
    public List<Day> days = new ArrayList<>();

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDayBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_day,
                parent,
                false
        );
        return new DayViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        Day day = days.get(position);
        holder.bind(day);
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public void setDays(List<Day> days) {
        if (days == null) {
            this.days = new ArrayList<>();
        } else {
            this.days = days;
        }
        notifyDataSetChanged();
    }

    static class DayViewHolder extends RecyclerView.ViewHolder {
        ItemDayBinding binding;

        public DayViewHolder(ItemDayBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Day day) {
            binding.setDay(day);
            binding.executePendingBindings();
        }
    }
}
