package com.example.RecordTime;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.RecordTime.Room.Database;
import com.example.RecordTime.Room.TimeTable_Dao;
import com.example.RecordTime.Room.TimeTable_RoomEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DailyCalendarFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.daily_calendar_fragment, container, false);
        loadDB(view);
        return view;
    }

    public void loadDB(View view) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.submit(new Runnable() {
            @Override
            public void run() {
                Database database = Room.databaseBuilder(getContext(), Database.class, "TimeTable").build();

                TimeTable_Dao dao = database.timeTable_dao();
                dao.insert(new TimeTable_RoomEntity("0番目のtitle"));
                dao.insert(new TimeTable_RoomEntity("1番目のtitle"));
                dao.insert(new TimeTable_RoomEntity("2番目のtitle"));

                List<TimeTable_RoomEntity> allTimeTable = dao.getAll();

                setRecyclerView(view, allTimeTable);
            }
        });
    }


    public void setRecyclerView(View view, List<TimeTable_RoomEntity> allTimeTable) {
        // RecyclerView を生成して各時間を生成
        RecyclerView recyclerView = view.findViewById(R.id.time_recycler_view_container);
        // ①recycleView に layoutManager をセット
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        // ②Adapter を生成して RecyclerView にセット
        recyclerView.setAdapter(new Timely_RecyclerView_Adapter(getContext(), allTimeTable));
    }
}