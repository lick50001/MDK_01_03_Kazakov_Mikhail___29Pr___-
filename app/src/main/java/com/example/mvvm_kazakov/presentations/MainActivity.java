package com.example.mvvm_kazakov.presentations;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.mvvm_kazakov.R;
import com.example.mvvm_kazakov.databinding.ActivityMainBinding;
import com.example.mvvm_kazakov.datas.databases.DBContext;
import com.example.mvvm_kazakov.datas.databases.WeatherContext;
import com.example.mvvm_kazakov.datas.workers.WeatherWorker;
import com.example.mvvm_kazakov.viewmodels.DayViewModel;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    DayViewModel viewModel;
    DayAdapter adapter;
    DBContext _context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _context = new DBContext(this);
        if(WeatherContext.allDays().isEmpty()){
            onStartWorkerNow();
        }
        onStartWorker();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(DayViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        setupRecyclerView();
    }
    public void onStartWorker() {
        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(
                WeatherWorker.class,
                15, TimeUnit.MINUTES,
                30, TimeUnit.SECONDS)
                .build();
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "WORKER_MANAGER",
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
        );
    }

    public void onStartWorkerNow() {
        OneTimeWorkRequest immediateWork = new OneTimeWorkRequest.Builder(WeatherWorker.class)
                .build();
        WorkManager.getInstance(this).enqueue(immediateWork);
    }
    private void setupRecyclerView(){
        if(adapter == null){
            adapter = new DayAdapter();
            binding.recyclerView.setAdapter(adapter);
        }else{
            adapter = (DayAdapter) binding.recyclerView.getAdapter();
        }
        viewModel.days.observe(this, days ->{
            if (days != null) {
                adapter.setDays(days);
            }else{
                adapter.setDays(new ArrayList<>());
            }
        });
    }
}