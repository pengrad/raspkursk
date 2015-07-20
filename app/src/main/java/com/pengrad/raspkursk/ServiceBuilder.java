package com.pengrad.raspkursk;

import android.util.Log;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

/**
 * stas
 * 7/18/15
 */
public class ServiceBuilder {

    public static YandexRaspService yandexRaspService() {

        RequestInterceptor requestInterceptor = request -> {
            request.addQueryParam("apikey", "4e6d8aff-67c2-458a-948b-3c449ef33ee4");
            request.addQueryParam("format", "json");
            request.addQueryParam("lang", "ru");
        };

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://api.rasp.yandex.net/v1.0")
                .setRequestInterceptor(requestInterceptor)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(message -> Log.d("YandexRaspService", message))
                .build();

        return restAdapter.create(YandexRaspService.class);
    }
}
