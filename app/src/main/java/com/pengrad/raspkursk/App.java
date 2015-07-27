package com.pengrad.raspkursk;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;


/**
 * stas
 * 7/17/15
 */
public class App extends Application {

    private static App instance;

    public static YandexRaspApi getYandexRaspApi() {
        return instance.mYandexRaspApi;
    }

    private YandexRaspApi mYandexRaspApi;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = (App) getApplicationContext();
        mYandexRaspApi = buildApi();
        LeakCanary.install(this);
    }


    private YandexRaspApi buildApi() {
        return ServiceBuilder.yandexRaspService(BuildConfig.YANDEX_API_KEY);
//        return new TestApi();
    }

}
