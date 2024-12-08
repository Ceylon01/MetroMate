package com.metromate.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.metromate.R;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder> {

    private List<String> favoritesList;

    public FavoritesAdapter(List<String> favoritesList) {
        this.favoritesList = favoritesList;
    }

    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent, false);
        return new FavoritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesViewHolder holder, int position) {
        String stationName = favoritesList.get(position);
        holder.stationNameTextView.setText(stationName);

        // 삭제 버튼 클릭 시 해당 역 삭제
        holder.deleteButton.setOnClickListener(v -> {
            favoritesList.remove(position);
            notifyItemRemoved(position);
        });
    }

    @Override
    public int getItemCount() {
        return favoritesList.size();
    }

    public static class FavoritesViewHolder extends RecyclerView.ViewHolder {
        TextView stationNameTextView;
        Button deleteButton;  // 삭제 버튼

        public FavoritesViewHolder(View itemView) {
            super(itemView);
            stationNameTextView = itemView.findViewById(R.id.stationNameTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);  // 삭제 버튼 초기화
        }
    }
}
