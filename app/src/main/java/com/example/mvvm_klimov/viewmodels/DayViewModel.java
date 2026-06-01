package com.example.mvvm_klimov.viewmodels;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mvvm_klimov.datas.apis.WeatherApi;
import com.example.mvvm_klimov.datas.apis.WeatherResponse;
import com.example.mvvm_klimov.datas.callbacks.MyResponseCallback;
import com.example.mvvm_klimov.datas.databases.WeatherContext;
import com.example.mvvm_klimov.domains.models.Day;
import com.example.mvvm_klimov.presentations.utils.DataNotifier;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DayViewModel extends ViewModel {
    MutableLiveData<List<Day>> _days = new MutableLiveData<>();
    public LiveData<List<Day>> days = _days;
    MutableLiveData<String> _nowTemp = new MutableLiveData<>();
    public LiveData<String> nowTemp = _nowTemp;
    MutableLiveData<String> _condition = new MutableLiveData<>();
    public LiveData<String> condition = _condition;

    public DayViewModel() {
        loadDays();
        DataNotifier.getInstance().subscribe(this::loadDays);
    }

    public void loadDays() {
        new Thread(() -> {
            List<Day> days = WeatherContext.allDays();

            new Handler(Looper.getMainLooper()).post(() -> {
                _days.setValue(days);
                if (days.isEmpty() == false) {
                    _nowTemp.setValue(days.get(0).Temp + "°");
                    _condition.setValue(days.get(0).Condition);
                }
            });
        }).start();
    }
}
