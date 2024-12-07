package com.metromate.PathFinding;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.metromate.models.SubwayData;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

public class SubwayJsonProcessor {

    public static SubwayData loadSubwayData(Context context) {
        try {
            // JSON 파일 로드
            InputStream inputStream = context.getAssets().open("data/subway_data.json");
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            // JSON 데이터 파싱
            Gson gson = new Gson();
            Type subwayDataType = new TypeToken<SubwayData>() {}.getType(); // 수정된 부분
            return gson.fromJson(json, subwayDataType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
