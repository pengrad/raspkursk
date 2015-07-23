package com.pengrad.raspkursk;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * stas
 * 7/17/15
 */
public interface YandexRaspApi {

    @GET("/search/?transport_type=suburban")
    Observable<SearchResponse> search(@Query("from") String fromStation, @Query("to") String toStation, @Query("date") String date);

    @GET("/thread/")
    Observable<ThreadResponse> thread(@Query("uid") String uid);

//    @GET("/schedule/?transport_type=suburban")
//    ScheduleResponse schedule(@Query("station") String station, @Query("date") String date);


}

