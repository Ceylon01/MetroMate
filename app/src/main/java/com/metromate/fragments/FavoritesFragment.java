package com.metromate.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.metromate.R;
import com.metromate.adapters.FavoritesAdapter;
import com.metromate.models.FavoriteStation;
import com.metromate.utils.FavoriteManager;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {

    private FavoritesAdapter favoritesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 레이아웃을 인플레이트
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        // getContext()가 null일 수 있으므로 null 체크
        if (getContext() != null) {
            // FavoriteManager 초기화
            FavoriteManager favoriteManager = new FavoriteManager(getContext());

            // UI 요소 참조
            RecyclerView favoriteRecyclerView = view.findViewById(R.id.favorite_list_view);

            // RecyclerView 설정
            favoriteRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            updateFavoriteList(favoriteManager, favoriteRecyclerView);
        }

        return view;
    }

    private void updateFavoriteList(FavoriteManager favoriteManager, RecyclerView favoriteRecyclerView) {
        List<Object> favoriteItems = new ArrayList<>();

        // FavoriteStation과 FavoriteRoute을 모두 리스트에 추가
        favoriteItems.addAll(favoriteManager.getFavoriteStations());
        favoriteItems.addAll(favoriteManager.getFavoriteRoutes());

        // RecyclerView 어댑터 설정
        favoritesAdapter = new FavoritesAdapter(favoriteItems, favoriteManager);
        favoriteRecyclerView.setAdapter(favoritesAdapter);
    }
}
