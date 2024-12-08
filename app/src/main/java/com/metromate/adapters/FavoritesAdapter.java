package com.metromate.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.metromate.R;
import com.metromate.models.FavoriteRoute;
import com.metromate.models.FavoriteStation;
import com.metromate.utils.FavoriteManager;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder> {

    private static final int TYPE_STATION = 0;
    private static final int TYPE_ROUTE = 1;

    private List<Object> favoriteItems;  // 역과 경로를 모두 관리
    private FavoriteManager favoriteManager;

    public FavoritesAdapter(List<Object> favoriteItems, FavoriteManager favoriteManager) {
        this.favoriteItems = favoriteItems;
        this.favoriteManager = favoriteManager;
    }

    @Override
    public int getItemViewType(int position) {
        if (favoriteItems.get(position) instanceof FavoriteStation) {
            return TYPE_STATION;
        } else {
            return TYPE_ROUTE;
        }
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_STATION) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_station, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_route, parent, false);
        }
        return new FavoriteViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Object item = favoriteItems.get(position);

        if (holder.getItemViewType() == TYPE_STATION && item instanceof FavoriteStation) {
            FavoriteStation station = (FavoriteStation) item;
            holder.stationNameTextView.setText(station.getName());

            // X 버튼 클릭 시 해당 역 삭제
            holder.deleteButton.setOnClickListener(v -> {
                favoriteManager.removeFavoriteStation(station);  // FavoriteStation 객체 삭제
                favoriteItems.remove(position);  // 리스트에서 삭제
                notifyItemRemoved(position);  // RecyclerView 갱신
            });
        } else if (holder.getItemViewType() == TYPE_ROUTE && item instanceof FavoriteRoute) {
            FavoriteRoute route = (FavoriteRoute) item;
            holder.routeNameTextView.setText(route.getRouteStations().toString());

            // X 버튼 클릭 시 해당 경로 삭제
            holder.deleteButton.setOnClickListener(v -> {
                favoriteManager.removeFavoriteRoute(route);  // FavoriteRoute 객체 삭제
                favoriteItems.remove(position);  // 리스트에서 삭제
                notifyItemRemoved(position);  // RecyclerView 갱신
            });
        }
    }

    @Override
    public int getItemCount() {
        return favoriteItems.size();
    }

    // FavoriteViewHolder는 역과 경로의 뷰 홀더를 하나로 병합
    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        TextView stationNameTextView;  // FavoriteStation을 위한 TextView
        TextView routeNameTextView;    // FavoriteRoute을 위한 TextView
        ImageView deleteButton;        // 삭제 버튼 (ImageView로 변경)

        public FavoriteViewHolder(View itemView, int viewType) {
            super(itemView);

            if (viewType == TYPE_STATION) {
                stationNameTextView = itemView.findViewById(R.id.stationNameTextView);
                routeNameTextView = null; // Route에서는 사용하지 않음
            } else {
                stationNameTextView = null; // Station에서는 사용하지 않음
                routeNameTextView = itemView.findViewById(R.id.routeNameTextView);
            }

            deleteButton = itemView.findViewById(R.id.deleteButton);  // 삭제 버튼
        }
    }
}
