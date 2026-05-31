package com.example.mvvm_klimov.datas.apis;

import android.os.AsyncTask;
import android.util.Log;

import com.example.mvvm_klimov.datas.callbacks.MyResponseCallback;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

public class WeatherApi extends AsyncTask<Void, Void, String> {
    String url;
    MyResponseCallback callback;

    public WeatherApi(double lat, double lon, MyResponseCallback callback) {
        this.url = "https://api.weather.yandex.ru/v2/forecast?lat=" + lat + "&lon=" + lon;
        this.callback = callback;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            Connection.Response response = Jsoup.connect(url)
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .method(Connection.Method.GET)
                    .header("X-Yandex-Weather-Key", "fe0544d6-d85a-4813-923d-ce7907278f9e")
                    .execute();

            return response.statusCode() == 200 ? response.body() : "Error: " + response.body();
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }

    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (result.startsWith("Error")) {
            Log.e("WeatherApi", result);
            callback.onError(result);
        } else {
            Log.d("WeatherApi", result);
            callback.onCompile(result);
        }
    }
}
