package com.example.metro_mate.data

import android.content.Context
import org.json.JSONObject

data class Station(val id: Int, val name: String, val line: String)

class SubwayDataLoader(private val context: Context) {
    fun loadSubwayData(): Pair<List<Station>, List<Edge>> {
        // JSON 파일 읽기
        val jsonData = context.assets.open("data/subway_data.json").bufferedReader().use { it.readText() }
        val jsonObject = JSONObject(jsonData)

        val stations = mutableListOf<Station>()
        val edges = mutableListOf<Edge>()

        // 역 데이터 로드
        jsonObject.getJSONArray("stations").let { array ->
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                stations.add(
                    Station(
                        id = obj.getInt("id"),
                        name = obj.getString("name"),
                        line = obj.getString("line")
                    )
                )
            }
        }

        // 간선 데이터 로드
        jsonObject.getJSONArray("edges").let { array ->
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                edges.add(
                    Edge(
                        start = obj.getInt("start"),
                        end = obj.getInt("end"),
                        time = obj.getInt("time"),
                        distance = obj.getInt("distance"),
                        cost = obj.getInt("cost")
                    )
                )
            }
        }

        return Pair(stations, edges)
    }
}