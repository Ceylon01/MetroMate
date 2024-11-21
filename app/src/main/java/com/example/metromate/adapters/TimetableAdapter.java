package com.example.metromate.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.metromate.R;

import java.util.List;

public class TimetableAdapter extends RecyclerView.Adapter<TimetableAdapter.ViewHolder> {

    private final List<String> timetableList;

    public TimetableAdapter(List<String> timetableList) {
        this.timetableList = timetableList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timetable, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String time = timetableList.get(position);
        holder.timetableTextView.setText(time);
    }

    @Override
    public int getItemCount() {
        return timetableList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView timetableTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            timetableTextView = itemView.findViewById(R.id.timetable_text);
        }
    }
}
