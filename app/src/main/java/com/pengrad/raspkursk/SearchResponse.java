package com.pengrad.raspkursk;

import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.List;

/**
 * stas
 * 7/18/15
 */
public class SearchResponse {

    public List<Thread> threads;

    @Override
    public String toString() {
        return Arrays.toString(threads.toArray());
    }

    public static class Thread {
        public String arrival;
        public String departure;
        public double duration;

        private JsonObject thread;
        private String uid;
        private String title;

        public String uid() {
            if (uid == null) {
                uid = thread.getAsJsonPrimitive("uid").getAsString();
            }
            return uid;
        }

        public String title() {
            if (title == null) {
                title = thread.getAsJsonPrimitive("title").getAsString();
            }
            return title;
        }

        @Override
        public String toString() {
            return uid() + " " + title() + " departure: " + departure + " arrival: " + arrival + " " + thread.toString();
        }
    }
}
