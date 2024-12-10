package com.metromate.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.metromate.R;
import com.metromate.adapters.FavoritesAdapter;
import com.metromate.models.FavoriteStation;
import com.metromate.utils.FavoriteManager;

import java.util.List;

public class FavoritesFragment extends Fragment {

    private FavoritesAdapter favoritesAdapter;
    private LinearLayout emptyStateContainer; // 빈 상태를 표시하는 컨테이너
    private RecyclerView favoriteRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 레이아웃 인플레이트
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        // UI 요소 초기화
        emptyStateContainer = view.findViewById(R.id.empty_state_container);
        favoriteRecyclerView = view.findViewById(R.id.favorite_list_view);

        if (getContext() != null) {
            // FavoriteManager 초기화
            FavoriteManager favoriteManager = new FavoriteManager(getContext());

            // RecyclerView 설정
            favoriteRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            updateFavoriteList(favoriteManager);
        }

        return view;
    }

    private void updateFavoriteList(FavoriteManager favoriteManager) {
        // 즐겨찾기 역 목록 가져오기
        List<FavoriteStation> favoriteStations = favoriteManager.getFavoriteStations();

        // 빈 상태 확인 및 UI 업데이트
        if (favoriteStations.isEmpty()) {
            // 빈 상태일 경우
            emptyStateContainer.setVisibility(View.VISIBLE);
            favoriteRecyclerView.setVisibility(View.GONE);
        } else {
            // 데이터가 있는 경우
            emptyStateContainer.setVisibility(View.GONE);
            favoriteRecyclerView.setVisibility(View.VISIBLE);

            // RecyclerView 어댑터 설정
            if (favoritesAdapter == null) {
                favoritesAdapter = new FavoritesAdapter(favoriteStations, favoriteManager, getContext());
                favoriteRecyclerView.setAdapter(favoritesAdapter);
            } else {
                favoritesAdapter.updateData(favoriteStations);
            }
        }
    }
}
