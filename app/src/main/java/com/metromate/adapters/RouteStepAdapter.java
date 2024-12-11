package com.metromate.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.metromate.R;
import com.metromate.models.Edge;
import com.metromate.models.Station;

import java.util.List;
import java.util.Map;

public class RouteStepAdapter extends RecyclerView.Adapter<RouteStepAdapter.PathViewHolder> {

    private final List<Station> stations; // 역 리스트
    private final List<Edge> edges; // Edge 리스트 (역 간 거리/시간 정보)
    private final Map<String, Integer> lineColorMap; // 노선별 색상 맵
    private final Context context; // DP -> PX 변환에 필요

    public RouteStepAdapter(Context context, List<Station> stations, List<Edge> edges, Map<String, Integer> lineColorMap) {
        this.context = context;
        this.stations = stations;
        this.edges = edges;
        this.lineColorMap = lineColorMap;
    }

    @NonNull
    @Override
    public PathViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_route_step, parent, false);
        return new PathViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PathViewHolder holder, int position) {
        Station station = stations.get(position);

        // 역 이름 설정
        holder.stationNameTextView.setText(station.getName());

        // 노선 색상 설정
        int color1 = lineColorMap.getOrDefault(station.getLine(), lineColorMap.get("default"));
        holder.lineIndicator1.setBackgroundTintList(ColorStateList.valueOf(color1));

        // 환승 여부 확인 및 두 번째 노선 색상 설정
        if (isTransferStation(station)) {
            String transferLine = getSecondLine(station);
            int color2 = lineColorMap.getOrDefault(transferLine, lineColorMap.get("default"));
            holder.lineIndicator2.setBackgroundTintList(ColorStateList.valueOf(color2));
            holder.lineIndicator2.setVisibility(View.VISIBLE);
        } else {
            holder.lineIndicator2.setVisibility(View.GONE);
        }

        // 거리 또는 시간에 따라 LineIndicator 길이 조정 (세로 방향)
        if (position < stations.size() - 1) {
            double distance = calculateDistanceBetweenStations(stations.get(position), stations.get(position + 1));
            int lineHeight = convertDistanceToLength(distance); // 거리 -> 높이 변환

            ViewGroup.LayoutParams params = holder.lineIndicator1.getLayoutParams();
            params.height = dpToPx(lineHeight, context); // 높이를 DP -> PX로 변환
            holder.lineIndicator1.setLayoutParams(params);

            if (holder.lineIndicator2.getVisibility() == View.VISIBLE) {
                ViewGroup.LayoutParams params2 = holder.lineIndicator2.getLayoutParams();
                params2.height = dpToPx(lineHeight, context);
                holder.lineIndicator2.setLayoutParams(params2);
            }
        } else {
            holder.lineIndicator1.setVisibility(View.GONE);
            holder.lineIndicator2.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return stations.size();
    }

    private double calculateDistanceBetweenStations(Station start, Station end) {
        for (Edge edge : edges) {
            if ((edge.getStart() == start.getId() && edge.getEnd() == end.getId()) ||
                    (edge.getStart() == end.getId() && edge.getEnd() == start.getId())) {
                return edge.getDistance(); // 거리 반환
            }
        }
        return 0; // 기본값
    }

    private int convertDistanceToLength(double distance) {
        int baseLengthDp = 80; // 1km당 80dp
        double scale = distance / 1000.0; // 거리(m)를 km로 변환
        return (int) (baseLengthDp * scale);
    }

    private int dpToPx(int dp, Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    private boolean isTransferStation(Station station) {
        return station.getLines() != null && station.getLines().size() > 1;
    }

    private String getSecondLine(Station station) {
        if (isTransferStation(station)) {
            List<String> lines = station.getLines();
            return lines.size() > 1 ? lines.get(1) : null;
        }
        return null;
    }

    public static class PathViewHolder extends RecyclerView.ViewHolder {
        View lineIndicator1, lineIndicator2;
        TextView stationNameTextView;

        public PathViewHolder(@NonNull View itemView) {
            super(itemView);
            lineIndicator1 = itemView.findViewById(R.id.line_indicator_1);
            lineIndicator2 = itemView.findViewById(R.id.line_indicator_2);
            stationNameTextView = itemView.findViewById(R.id.tv_station_name);
        }
    }
}
