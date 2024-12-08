package com.metromate.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import androidx.fragment.app.Fragment;
import com.metromate.R;

public class TimetableFragment extends Fragment {

    private Spinner lineSpinner;
    private ImageView timetableImage;

    public TimetableFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timetable, container, false);

        // Initialize Spinner and ImageView
        lineSpinner = view.findViewById(R.id.line_spinner);
        timetableImage = view.findViewById(R.id.timetable_image);

        // Set up Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.line_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lineSpinner.setAdapter(adapter);

        // Set up listener for Spinner
        lineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Update the timetable image based on selected line
                switch (position) {
                    case 0: // 1호선
                        timetableImage.setImageResource(R.drawable.timetable_1);
                        break;
                    case 1: // 2호선
                        timetableImage.setImageResource(R.drawable.timetable_2);
                        break;
                    case 2: // 3호선
                        timetableImage.setImageResource(R.drawable.timetable_3);
                        break;
                    case 3: // 4호선
                        timetableImage.setImageResource(R.drawable.timetable_4);
                        break;
                    default:
                        timetableImage.setImageResource(0); // No image
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case where no selection is made
            }
        });

        return view;
    }
}