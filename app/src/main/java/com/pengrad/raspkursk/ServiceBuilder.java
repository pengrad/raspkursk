package com.pengrad.raspkursk;

import android.util.Log;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

/**
 * stas
 * 7/18/15
 */
public class ServiceBuilder {

    public static YandexRaspApi yandexRaspService(String apikey) {

        RequestInterceptor requestInterceptor = request -> {
            request.addQueryParam("apikey", apikey);
            request.addQueryParam("format", "json");
            request.addQueryParam("lang", "ru");
        };

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://api.rasp.yandex.net/v1.0")
                .setRequestInterceptor(requestInterceptor)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(message -> Log.d("YandexRaspApi", message))
                .build();

        return restAdapter.create(YandexRaspApi.class);
    }
}
