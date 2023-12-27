package com.example.RecordTime;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.RecordTime.Rooms.Database;
import com.example.RecordTime.Rooms.TimeTable;
import com.example.RecordTime.Rooms.TimeTable_Dao;
import com.example.RecordTime.Rooms.TimeTable_RoomEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DailyCalendarFragment extends Fragment {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private Handler handler;
    private Timely_RecyclerView_Adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.daily_calendar_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        adapter = new Timely_RecyclerView_Adapter();

        RecyclerView recyclerView = view.findViewById(R.id.time_recycler_view_container);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        handler = new Handler(Looper.getMainLooper());

        loadDB();
    }

    private void loadDB() {
        executor.submit(() -> {
//            handler.post(() -> adapter.load());
        });
    }
}