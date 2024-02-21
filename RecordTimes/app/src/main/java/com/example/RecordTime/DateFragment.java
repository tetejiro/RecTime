package com.example.RecordTime;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.RecordTime.Rooms.AppDatabase;
import com.example.RecordTime.Rooms.TimeTableDao;
import com.example.RecordTime.Rooms.TimeTableEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class DateFragment extends Fragment {

    List<TimeTableEntity> timeTableEntities;
    List<TimeTableEntity> returnedTimeTableEntities = new ArrayList<>();

    TimeTableDao timeTableDao;

    InputMethodManager inputMethodManager;

    View view;

    int year;
    int month;
    int date;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        timeTableEntities = new ArrayList<>();
        timeTableEntities.add(new TimeTableEntity("0番名のレコード"));
        timeTableEntities.add(new TimeTableEntity("1番名のレコード"));
        timeTableEntities.add(new TimeTableEntity("2番名のレコード"));

        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_date, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        this.view = view;

        getParentFragmentManager().setFragmentResultListener("date", this, new FragmentResultListener() {

            @Override
            public void onFragmentResult(@NonNull String key, @NonNull Bundle bundle) {
                LocalDate localDate = (LocalDate) bundle.getSerializable("date");
                year = localDate.getYear();
                month = localDate.getMonthValue();
                date = localDate.getDayOfMonth();

                // 〇年・〇月・〇日をセット
                setDateText();
            }
        });

        Thread thread = new Thread(new Query());
        thread.start();

        try {
            thread.join();

            // RecyclerView をセット
            RecyclerView recyclerView = view.findViewById(R.id.time_table_recycler_view);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(new Adapter());

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // モーダル：開ける
        FloatingActionButton modalButton = (FloatingActionButton)view.findViewById(R.id.floatingActionButton);
        modalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 日付フラグメントの上にモーダルフラグメントを置く
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)//トランザクションに関与するフラグメントの状態変更を最適化
                        .add(R.id.activity_fragment_container, new ModalFragment(), "modalFragment")
                        .addToBackStack("DateFragment")
                        .commit();
            }
        });

        // キーボードを閉じる（フラグメント内）
        RecyclerView recyclerView = view.findViewById(R.id.time_table_recycler_view);
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //キーボードを非表示にする
                inputMethodManager.hideSoftInputFromWindow(recyclerView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                return false;
            }
        });

        // TODO: モーダルの外をタップ・キーボード非表示時にのみモーダルフラグメントを外す。
    }

    public class Query implements Runnable {

        @Override
        public void run() {
            AppDatabase database = Room.databaseBuilder(getActivity().getApplicationContext(),
                    AppDatabase.class, "TimeTable").build();
            timeTableDao = database.timeTableDao();
            timeTableDao.insertAll(timeTableEntities);
            returnedTimeTableEntities = timeTableDao.getAll();
        }
    }

    public void setDateText() {

        // 〇年をセット
        TextView year_text = view.findViewById(R.id.selected_year);
        year_text.setText(year + " 年");

        // 〇月をセット
        TextView month_text = view.findViewById(R.id.selected_month);
        month_text.setText(month + " 月");

        // 〇日をセット
        TextView date_text = view.findViewById(R.id.selected_date);
        date_text.setText(date + " 日");
    }

    class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView textView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                this.textView = itemView.findViewById(R.id.every_time_table);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_time_table, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.textView.setText(timeTableEntities.get(position).title + " / " + formatDateTime(timeTableEntities.get(position).datetime));
            if (timeTableEntities.get(position).done) holder.textView.setBackgroundColor(Color.rgb(124,252,0)); // 赤
            else holder.textView.setBackgroundColor(Color.rgb(249,247,57)); // 黄色
        }

        private String formatDateTime(LocalDateTime date) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            return date.format(formatter);
        }

        @Override
        public int getItemCount() {
            return timeTableEntities.size();
        }
    }
}