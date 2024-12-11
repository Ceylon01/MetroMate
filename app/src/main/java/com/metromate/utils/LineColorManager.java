package com.metromate.utils;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.metromate.R;

import java.util.HashMap;
import java.util.Map;

public class LineColorManager {

    public static Map<String, Integer> getLineColors(Context context) {
        Map<String, Integer> lineColors = new HashMap<>();
        lineColors.put("1", ContextCompat.getColor(context, R.color.line1_color)); // 1호선
        lineColors.put("2", ContextCompat.getColor(context, R.color.line2_color)); // 2호선
        lineColors.put("3", ContextCompat.getColor(context, R.color.line3_color)); // 3호선
        lineColors.put("4", ContextCompat.getColor(context, R.color.line4_color)); // 4호선
        lineColors.put("default", ContextCompat.getColor(context, R.color.colorPrimaryVariant)); // 기본 색
        return lineColors;
    }

}

