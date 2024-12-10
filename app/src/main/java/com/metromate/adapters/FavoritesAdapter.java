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

import java.util.ArrayList;
import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder> {

    private List<FavoriteStation> favoriteStations;
    private final FavoriteManager favoriteManager;
    private final Context context;

    public FavoritesAdapter(List<FavoriteStation> favoriteStations, FavoriteManager favoriteManager, Context context) {
        this.favoriteStations = new ArrayList<>(favoriteStations); // 불변성 보장을 위해 복사본 생성
        this.favoriteManager = favoriteManager;
        this.context = context;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_station, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        final FavoriteStation station = favoriteStations.get(position);
        holder.stationNameTextView.setText(station.getName());

        // 역 클릭 시 SearchActivity로 이동
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SearchActivity.class);
            intent.putExtra("stationName", station.getName());
            context.startActivity(intent);
        });

        // X 버튼 클릭 시 해당 역 삭제
        holder.deleteButton.setOnClickListener(v -> {
            removeStation(position); // 삭제 로직 분리
        });
    }

    @Override
    public int getItemCount() {
        return favoriteStations.size();
    }

    /**
     * 데이터 업데이트 메서드: 새로운 리스트로 갱신
     */
    public void updateData(List<FavoriteStation> newFavoriteStations) {
        this.favoriteStations = new ArrayList<>(newFavoriteStations); // 새로운 데이터로 교체
        notifyDataSetChanged(); // 전체 업데이트
    }

    /**
     * 리스트에서 역 삭제 및 RecyclerView 업데이트
     */
    private void removeStation(int position) {
        if (position < 0 || position >= favoriteStations.size()) return;

        // FavoriteManager에서 역 삭제
        FavoriteStation stationToRemove = favoriteStations.get(position);
        favoriteManager.removeFavoriteStation(stationToRemove);

        // 리스트에서 삭제 후 RecyclerView 갱신
        favoriteStations.remove(position);
        notifyItemRemoved(position);

        // notifyItemRangeChanged로 삭제 후의 뷰 위치 업데이트
        notifyItemRangeChanged(position, favoriteStations.size());
    }

    // FavoriteViewHolder 클래스
    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        TextView stationNameTextView;
        ImageView deleteButton;

        public FavoriteViewHolder(View itemView) {
            super(itemView);
            stationNameTextView = itemView.findViewById(R.id.stationNameTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
