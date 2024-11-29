package com.metromate.PathFinding;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.metromate.models.Edge;
import com.metromate.models.SubwayData;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SubwayJsonProcessor {
    public static void processJson(String inputFilePath, String outputFilePath) {
        try {
            // JSON 파일 읽기
            BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
            Gson gson = new Gson();
            Type subwayDataType = new TypeToken<SubwayData>() {}.getType();
            SubwayData subwayData = gson.fromJson(reader, subwayDataType);
            reader.close();

            // 역방향 경로 추가
            List<Edge> originalEdges = subwayData.edges;
            List<Edge> updatedEdges = new ArrayList<>(originalEdges);

            for (Edge edge : originalEdges) {
                boolean reverseExists = updatedEdges.stream().anyMatch(
                        e -> e.getStart() == edge.getEnd() &&
                                e.getEnd() == edge.getStart() &&
                                e.getTime() == edge.getTime() &&
                                e.getDistance() == edge.getDistance()
                );

                if (!reverseExists) {
                    updatedEdges.add(new Edge(edge.getEnd(), edge.getStart(), edge.getTime(), edge.getDistance()));
                }
            }

            subwayData.edges = updatedEdges;

            // 수정된 데이터 저장
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));
            gson.toJson(subwayData, writer);
            writer.close();

            System.out.println("역방향 경로가 추가된 JSON 파일이 저장되었습니다: " + outputFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
