package com.example.metromate.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.metromate.R;
import com.example.metromate.adapters.TimetableAdapter;

import java.util.ArrayList;
import java.util.List;

public class TimetableFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_timetable, container, false);

        // RecyclerView 설정
        RecyclerView recyclerView = rootView.findViewById(R.id.timetable_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 데이터 추가
        List<String> timetableData = new ArrayList<>();
        timetableData.add("06:00 - Train 1");
        timetableData.add("06:30 - Train 2");
        timetableData.add("07:00 - Train 3");

        // 어댑터 설정
        TimetableAdapter adapter = new TimetableAdapter(timetableData);
        recyclerView.setAdapter(adapter);

        return rootView;
    }
}
