package com.metromate.fare;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.metromate.models.Station;
import com.metromate.models.SubwayData;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubwayFareLoader {

    public static void loadSubwayFareData(Context context, OnDataLoadedListener listener) {
        new Thread(() -> {
            try {
                InputStream inputStream = context.getAssets().open("data/subway_fare.json");
                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);
                inputStream.close();
                String json = new String(buffer, StandardCharsets.UTF_8);
                Gson gson = new Gson();
                SubwayData subwayData = gson.fromJson(json, SubwayData.class);
                listener.onDataLoaded(subwayData);
                Log.d("SubwayFareLoader", "데이터 로드 성공");
            } catch (Exception e) {
                e.printStackTrace();
                listener.onDataLoadFailed(e);
                Log.e("SubwayFareLoader", "데이터 로드 실패", e);
            }
        }).start();
    }

    public interface OnDataLoadedListener {
        void onDataLoaded(SubwayData subwayData);
        void onDataLoadFailed(Exception e);
    }
}
