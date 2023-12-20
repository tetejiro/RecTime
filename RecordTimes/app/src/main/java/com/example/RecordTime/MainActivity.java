package com.example.RecordTime;

import androidx.fragment.app.FragmentActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;

import com.example.RecordTime.Room.Database;
import com.example.RecordTime.Room.TimeTable_Dao;
import com.example.RecordTime.Room.TimeTable_RoomEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends FragmentActivity {

    private List<TimeTable_RoomEntity> allTimeTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 月画面フラグメントを表示
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)//トランザクションに関与するフラグメントの状態変更を最適化
                .add(R.id.month_fragment_container, new MonthFragment())
                .commit();
    }
}