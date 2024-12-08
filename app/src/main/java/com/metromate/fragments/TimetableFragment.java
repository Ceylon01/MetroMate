package com.metromate.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.metromate.R;

import java.util.ArrayList;
import java.util.List;

public class TimetableFragment extends Fragment {

    private LinearLayout timetableLayout;
    private Spinner lineSpinner;
    private TextView timetableTitle;

    private String[] lineNames = {"1호선", "2호선", "3호선", "4호선"};
    private int startHour = 6;  // 시작 시간 (6시)
    private int startMinute = 9; // 시작 분 (9분)
    private int intervalMinutes = 8; // 시간 간격 (8분)

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // fragment_timetable.xml 레이아웃을 연결
        View rootView = inflater.inflate(R.layout.fragment_timetable, container, false);

        timetableLayout = rootView.findViewById(R.id.timetable_layout);
        lineSpinner = rootView.findViewById(R.id.line_spinner);
        timetableTitle = rootView.findViewById(R.id.timetable_title);

        // Spinner에 ArrayAdapter 설정
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, lineNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lineSpinner.setAdapter(adapter);

        // Spinner 아이템 선택 이벤트 리스너 설정
        lineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 선택된 호선에 맞는 시간표를 갱신
                updateTimetable(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 아무것도 선택되지 않았을 때 처리할 내용 (필요시 구현)
            }
        });

        // 초기 로딩 시 1호선 시간표 표시
        updateTimetable(0);

        return rootView;
    }

    private void updateTimetable(int lineIndex) {
        // 기존에 추가된 시간표 항목들을 지우고 새로운 시간표 추가
        timetableLayout.removeAllViews();

        // 선택된 호선에 맞는 시간표 생성 (8분 간격)
        List<String> timetable = generateTimetable();

        // 선택된 호선 시간표 추가
        for (String time : timetable) {
            TextView timeTextView = new TextView(getContext());
            timeTextView.setText(time);
            timeTextView.setTextSize(18);
            timeTextView.setPadding(16, 16, 16, 16);  // 여백 추가
            timetableLayout.addView(timeTextView);
        }

        // 시간표 제목 변경 (호선 이름)
        timetableTitle.setText(lineNames[lineIndex] + " 시간표");
    }

    private List<String> generateTimetable() {
        List<String> times = new ArrayList<>();
        int currentHour = startHour;
        int currentMinute = startMinute;

        while (currentHour < 23 || (currentHour == 23 && currentMinute <= 50)) {
            // 시간을 "HH:mm" 형식으로 변환
            String time = String.format("%02d:%02d", currentHour, currentMinute);
            times.add(time);

            // 8분 추가
            currentMinute += intervalMinutes;

            if (currentMinute >= 60) {
                currentMinute -= 60;
                currentHour += 1;
            }
        }

        return times;
    }
}