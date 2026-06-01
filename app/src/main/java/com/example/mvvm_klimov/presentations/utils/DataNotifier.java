package com.example.mvvm_klimov.presentations.utils;

import java.util.ArrayList;
import java.util.List;

public class DataNotifier {
    static DataNotifier _instanse;
    List<Runnable> _listeners = new ArrayList<>();

    public static DataNotifier getInstance() {
        if (_instanse == null) {
            _instanse = new DataNotifier();
        }
        return _instanse;
    }

    public void subscribe(Runnable listener) {
        _listeners.add(listener);
    }

    public void notifyUpdate() {
        for (Runnable listener : _listeners) {
            listener.run();
        }
    }
}
