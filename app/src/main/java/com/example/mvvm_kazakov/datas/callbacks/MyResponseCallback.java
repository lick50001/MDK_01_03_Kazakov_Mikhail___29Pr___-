package com.example.mvvm_kazakov.datas.callbacks;

public interface MyResponseCallback {
    void onCompile(String result);
    void onError(String error);
}