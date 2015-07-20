package com.pengrad.raspkursk;

import android.content.res.Resources;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * stas
 * 7/17/15
 */
public class StationManager {

    public static final String lgovCode = "s9600976";
    public static final String kurskCode = "s9600816";

    private Map<String, Station> stationsMap;

    public StationManager(Resources resources) {
        String[] stations = resources.getStringArray(R.array.stations);
        stationsMap = new HashMap<>(stations.length);
        for (String station : stations) {
            String[] splitResult = station.split("\\|", 2);
            stationsMap.put(splitResult[0], new Station(splitResult[0], splitResult[1]));
        }
    }

    public Station getStationByCode(String code) {
        return stationsMap.get(code);
    }

    public Station getDefaultFromStation() {
        return stationsMap.get(lgovCode);
    }

    public Station getDefaultToStation() {
        return stationsMap.get(kurskCode);
    }

    public Collection<Station> getAllStations() {
        return stationsMap.values();
    }
}
