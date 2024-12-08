package com.metromate.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.metromate.R;
import com.metromate.PathFinding.SearchActivity;
import com.metromate.models.FavoriteStation;
import com.metromate.utils.FavoriteManager;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder> {

    private List<FavoriteStation> favoriteStations;  // 즐겨찾기 역만 관리
    private FavoriteManager favoriteManager;
    private Context context;

    public FavoritesAdapter(List<FavoriteStation> favoriteStations, FavoriteManager favoriteManager, Context context) {
        this.favoriteStations = favoriteStations;
        this.favoriteManager = favoriteManager;
        this.context = context;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 역만 관리하므로 item_favorite_station 레이아웃만 사용
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_station, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        // 해당 역 정보를 가져옵니다.
        FavoriteStation station = favoriteStations.get(position);
        holder.stationNameTextView.setText(station.getName());

        // 역 클릭 시 SearchActivity로 이동
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SearchActivity.class);
            intent.putExtra("stationName", station.getName());  // 역 이름을 넘겨줍니다.
            context.startActivity(intent);
        });

        // X 버튼 클릭 시 해당 역 삭제
        holder.deleteButton.setOnClickListener(v -> {
            favoriteManager.removeFavoriteStation(station);  // FavoriteStation 객체 삭제
            favoriteStations.remove(position);  // 리스트에서 삭제
            notifyItemRemoved(position);  // RecyclerView 갱신
        });
    }

    @Override
    public int getItemCount() {
        return favoriteStations.size();  // 즐겨찾기 역의 개수 반환
    }

    // FavoriteViewHolder는 역을 위한 뷰 홀더
    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        TextView stationNameTextView;  // 역 이름을 위한 TextView
        ImageView deleteButton;        // 삭제 버튼 (ImageView로 변경)

        public FavoriteViewHolder(View itemView) {
            super(itemView);
            stationNameTextView = itemView.findViewById(R.id.stationNameTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);  // 삭제 버튼
        }
    }
}
