package com.example.RecordTime;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.RecordTime.Room.Database;
import com.example.RecordTime.Room.TimeTable_Dao;
import com.example.RecordTime.Room.TimeTable_RoomEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DateFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.date_fragment, container, false);

        // 〇月をセット
        TextView month_textview = view.findViewById(R.id.selected_month);
        month_textview.setText(getArguments().getString("month"));

        // 〇日をセット
        TextView date_textview = view.findViewById(R.id.selected_date);
        date_textview.setText(getArguments().getString("date"));

        return view;
    }
}