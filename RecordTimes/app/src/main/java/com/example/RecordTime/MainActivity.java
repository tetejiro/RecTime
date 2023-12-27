package com.example.RecordTime;

import androidx.fragment.app.FragmentActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;

import com.example.RecordTime.Rooms.Database;
import com.example.RecordTime.Rooms.TimeTable;
import com.example.RecordTime.Rooms.TimeTable_Dao;
import com.example.RecordTime.Rooms.TimeTable_RoomEntity;

import java.util.List;


public class MainActivity extends FragmentActivity {

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

    // 新規登録　開始
    public void registration_start(View view) {
        EditText editText = findViewById(R.id.contents);
//        editText.getText();
        try {
            Log.d("aaaaaaaaaa=====>", "aaaaaaaaaaaa");
            Database DB = Room.databaseBuilder(getApplicationContext(),
                    Database.class, "TimeTable").build();
            TimeTable_Dao dao = DB.timeTable_dao();
            Log.d("bbbbbbb=====>", "bbbbbbbbbb");

            dao.deleteAll();
            dao.insert(new TimeTable_RoomEntity("inserted title=======>"));
            Log.d("all========>", dao.getAll().get(0).title);

//            Timely_RecyclerView_Adapter adapter = new Timely_RecyclerView_Adapter();
//            adapter.init();
        } catch (Exception e) {
            Log.d("FAILUR=====>", "registration_start failed");
        }
    }

    // 新規登録　終了
    public void registration_end(View view) {
        EditText editText = findViewById(R.id.contents);
        Log.d("registration_end==>", editText.getText().toString());
    }
}