package com.example.mvvm_klimov.presentations;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.mvvm_klimov.R;
import com.example.mvvm_klimov.databinding.ActivityMainBinding;
import com.example.mvvm_klimov.datas.apis.WeatherApi;
import com.example.mvvm_klimov.datas.databases.DbContext;
import com.example.mvvm_klimov.datas.databases.WeatherContext;
import com.example.mvvm_klimov.datas.workers.WeatherWorker;
import com.example.mvvm_klimov.viewmodels.DayViewModel;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    DbContext _context;
    LocationManager locationManager;
    LocationListener locationListener;
    ActivityMainBinding binding;
    DayViewModel viewModel;
    DayAdapter adapter;
    public static double lastLat = 58.0;
    public static double lastLon = 56.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        _context = new DbContext(this);
//        if (WeatherContext.allDays().isEmpty()) {
//            onStartWorkerNow();
//        }
        onStartWorker();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(DayViewModel.class);

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        setupRecycleView();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            startLocationUpdates();
        }
    }

    public void onStartWorker() {
        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(
                WeatherWorker.class,
                1, TimeUnit.MINUTES,
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

    private void startLocationUpdates() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                double lat = location.getLatitude();
                double lon = location.getLongitude();

                lastLat = lat;
                lastLon = lon;

                OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(WeatherWorker.class).build();
                WorkManager.getInstance(MainActivity.this).enqueue(workRequest);
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000,
                    10,
                    locationListener
            );
        }
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