package com.example.mvvm_kazakov.viewmodels;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.mvvm_kazakov.datas.databases.WeatherContext;
import com.example.mvvm_kazakov.domains.models.Day;
import com.example.mvvm_kazakov.presentations.utils.DataNotifier;
import java.util.List;

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