package com.pengrad.raspkursk;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * stas
 * 7/17/15
 */
public class StationManager {

    public static final String lgovCode = "s9600976";
    public static final String kurskCode = "s9600816";

    private Map<String, Station> stationsMap;
    private ArrayList<Station> stations;

    public StationManager(Resources resources) {
        String[] stationsRes = resources.getStringArray(R.array.stations);
        stationsMap = new HashMap<>(stationsRes.length);
        stations = new ArrayList<>(stationsRes.length);
        for (String stationRes : stationsRes) {
            String[] splitResult = stationRes.split("\\|", 2);
            Station station = new Station(splitResult[0], splitResult[1]);
            stationsMap.put(splitResult[0], station);
            stations.add(station);
        }
    }

    public Station getStationByCode(String code) {
        return stationsMap.get(code);
    }

    public String getDefaultFromStationCode() {
        return lgovCode;
    }

    public String getDefaultToStationCode() {
        return kurskCode;
    }

    public List<Station> getAllStations() {
        return stations;
    }
}
