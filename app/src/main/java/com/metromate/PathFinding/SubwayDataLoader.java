package com.metromate.PathFinding;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.metromate.models.Edge;
import com.metromate.models.Station;
import com.metromate.models.SubwayData;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubwayDataLoader {

    public interface OnDataLoadedListener {
        void onDataLoaded(SubwayData subwayData);
    }

    public static void loadSubwayData(Context context, OnDataLoadedListener listener) {
        new Thread(() -> {
            try {
                InputStream inputStream = context.getAssets().open("data/subway_data.json");
                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);
                inputStream.close();

                String json = new String(buffer, StandardCharsets.UTF_8);
                Gson gson = new Gson();
                RawData rawData = gson.fromJson(json, RawData.class);

                SubwayData subwayData = new SubwayData(rawData.stations, rawData.edges);
                listener.onDataLoaded(subwayData);
            } catch (Exception e) {
                Log.e("SubwayDataLoader", "데이터 로드 실패: " + e.getMessage());
                listener.onDataLoaded(null);
            }
        }).start();
    }

    private static class RawData {
        List<Station> stations;
        List<Edge> edges;
    }
}
