package com.example.mvvm_klimov.presentations;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.mvvm_klimov.R;
import com.example.mvvm_klimov.databinding.ActivityMainBinding;
import com.example.mvvm_klimov.viewmodels.DayViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    DayViewModel viewModel;
    DayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(DayViewModel.class);

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        setupRecycleView();
    }

    private void setupRecycleView() {
        if (adapter == null) {
            adapter = new DayAdapter();
            binding.recycleView.setAdapter(adapter);
        } else {
            adapter = (DayAdapter) binding.recycleView.getAdapter();
        }

        viewModel.days.observe(this, days -> {
            if (days != null) {
                adapter.setDays(days);
            } else {
                adapter.setDays(new ArrayList<>());
            }
        });
    }
}