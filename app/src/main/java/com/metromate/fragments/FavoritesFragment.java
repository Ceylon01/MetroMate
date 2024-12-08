package com.metromate.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.libraries.mapsplatform.transportation.consumer.model.Route;
import com.metromate.FavoritesAdapter;
import com.metromate.R;

import java.util.ArrayList;

public class FavoritesFragment extends Fragment {

    private RecyclerView recyclerView;
    private FavoritesAdapter favoritesAdapter;
    private ArrayList<Route> favoriteRoutes;
    private DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        // RecyclerView 초기화
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // DatabaseHelper 초기화
        databaseHelper = new DatabaseHelper(getContext());

        // 즐겨찾기 데이터 로드
        favoriteRoutes = databaseHelper.getAllFavorites();

        // 어댑터 설정
        favoritesAdapter = new FavoritesAdapter(favoriteRoutes);
        recyclerView.setAdapter(favoritesAdapter);

        return view;
    }
}


