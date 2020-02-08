package com.pengrad.raspkursk;

public class TestApp extends App {

    @Override
    protected YandexRaspApi buildApi() {
        return new TestApi();
    }
}
