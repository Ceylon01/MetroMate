package com.metromate.PathFinding;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.metromate.models.Station;
import com.metromate.models.SubwayData;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

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

                SubwayData subwayData = new Gson().fromJson(json, SubwayData.class);
                new Handler(Looper.getMainLooper()).post(() -> listener.onDataLoaded(subwayData));

            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> listener.onDataLoaded(null));
            }
        }).start();
    }
}
