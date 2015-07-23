package com.pengrad.raspkursk;

import com.google.gson.JsonObject;

import java.util.List;

/**
 * stas
 * 7/19/15
 */
public class ThreadResponse {

    public List<Stop> stops;

    public static class Stop {
        public String arrival;
        public String departure;

        private JsonObject station;
        private String code;
        private String title;

        public String code() {
            if (code == null) {
                code = station.getAsJsonPrimitive("code").getAsString();
            }
            return code;
        }

        public String title() {
            if (title == null) {
                title = station.getAsJsonPrimitive("title").getAsString();
            }
            return title;
        }

    }

}
