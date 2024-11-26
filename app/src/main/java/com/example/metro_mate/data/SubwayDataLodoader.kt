package com.example.metro_mate.data

import android.content.Context
import android.util.Log
import org.json.JSONObject
import java.io.FileNotFoundException
import java.io.IOException

data class Station(val id: Int, val name: String, val line: String)

// Edge 클래스를 SubwayEdge로 이름 변경
data class SubwayEdge(val start: Int, val end: Int, val time: Int, val distance: Int, val cost: Int)

class SubwayDataLoader(private val context: Context) {

    fun loadSubwayData(): Pair<List<Station>, List<SubwayEdge>> {
        val stations = mutableListOf<Station>()
        val edges = mutableListOf<SubwayEdge>()

        try {
            // JSON 파일 읽기
            val jsonData = context.assets.open("data/subway_data.json").bufferedReader().use { it.readText() }
            val jsonObject = JSONObject(jsonData)

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
                        SubwayEdge(
                            start = obj.getInt("start"),
                            end = obj.getInt("end"),
                            time = obj.getInt("time"),
                            distance = obj.getInt("distance"),
                            cost = obj.getInt("cost")
                        )
                    )
                }
            }

        } catch (e: FileNotFoundException) {
            Log.e("SubwayDataLoader", "File not found: ${e.message}")
        } catch (e: IOException) {
            Log.e("SubwayDataLoader", "Error reading file: ${e.message}")
        } catch (e: Exception) {
            Log.e("SubwayDataLoader", "Unexpected error: ${e.message}")
        }

        return Pair(stations, edges)
    }
}
