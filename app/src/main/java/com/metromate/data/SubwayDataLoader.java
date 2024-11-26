package com.metromate.data;

import android.content.Context;
import android.content.res.AssetManager;
import com.google.gson.Gson;

import java.io.InputStreamReader;

public class SubwayDataLoader {
    public static SubwayData loadSubwayData(Context context) {
        try {
            // AssetManager를 사용하여 assets 폴더의 JSON 파일 열기
            AssetManager assetManager = context.getAssets();
            InputStreamReader reader = new InputStreamReader(assetManager.open("data/subway_data.json"));

            // Gson을 사용해 JSON 파싱
            Gson gson = new Gson();
            return gson.fromJson(reader, SubwayData.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

