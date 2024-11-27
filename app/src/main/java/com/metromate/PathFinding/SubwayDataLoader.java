package com.metromate.PathFinding;

import android.content.Context;
import android.content.res.AssetManager;
import com.google.gson.Gson;
import com.metromate.models.SubwayData;

import java.io.InputStreamReader;

public class SubwayDataLoader {
    public static SubwayData loadSubwayData(Context context) {
        try {
            AssetManager assetManager = context.getAssets();
            InputStreamReader reader = new InputStreamReader(assetManager.open("data/subway_data.json"));
            Gson gson = new Gson();
            return gson.fromJson(reader, SubwayData.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
