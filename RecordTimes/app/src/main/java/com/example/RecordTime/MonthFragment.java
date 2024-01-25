package com.example.RecordTime;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MonthFragment extends Fragment {
    View view;
    Calendar this_month_instance;
    int this_first_day_of_week;
    Integer this_last_day_of_week;
    int preMaxDate;
    int rest;

    private ArrayList<DateList> dateList = new ArrayList<>();
    Calendar cl = new GregorianCalendar();
    int this_year = cl.get(Calendar.YEAR);
    int this_month = cl.get(Calendar.MONTH);
    int this_date = cl.get(Calendar.DATE);
    int this_maxDate = cl.getActualMaximum(Calendar.DATE); // 今月の最大日数
    int idx = 0;

    @Override
    //container: fragment が挿入される親
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.month_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onStart();

        this.view = view;

        Button next = view.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(this_month == 11) {
                    this_month = 0;
                    this_year++;
                } else {
                    this_month++;
                }
                viewCalendar();
            }
        });

        Button prev = view.findViewById(R.id.prev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(this_month == 0) {
                    this_month = 11;
                    this_year--;
                } else {
                    this_month--;
                }
                viewCalendar();
            }
        });

        viewCalendar();
    }

    public void viewCalendar() {
        dataInitialize();

        // 〇年を今年にする
        TextView yearText = this.view.findViewById(R.id.year);
        yearText.setText(this_year + "年");
        // 〇月を今月にする
        TextView monthText = this.view.findViewById(R.id.month);
        monthText.setText(this_month + 1 + "月");

        // RecyclerView を生成して各日にちを生成
        RecyclerView recyclerView = this.view.findViewById(R.id.date_recycler_view_container);
        recyclerView.setHasFixedSize(true);
        // ①recycleView に layoutManager をセット
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 7));
        // ②Adapter を生成して RecyclerView にセット
        recyclerView.setAdapter(new Monthly_RecyclerView_Adapter(getActivity(), this.dateList));
    }

    // dateList = [DateList, , ...] を作成
    private void dataInitialize() {

        this.dateList.clear(); // push する対象

        this.idx = 0;
        // 今月1日のインスタンス
        this.this_month_instance = new GregorianCalendar(this.this_year, this.this_month, 1);
        // 今月1日の曜日
        this.this_first_day_of_week = this_month_instance.get(Calendar.DAY_OF_WEEK);
        // 今月最終日の曜日
        this.this_last_day_of_week = new GregorianCalendar(this.this_year, this.this_month, this.this_maxDate).get(Calendar.DAY_OF_WEEK);
        // 先月の最大日数
        this.preMaxDate = new GregorianCalendar(this.this_year, this.this_month-1, 1)
                                    .getActualMaximum(Calendar.DATE);

        //★① 1日よりも前の日付生成
        this.this_first_day_of_week = - (this.this_first_day_of_week - 1); // -(今月初日の曜日 - 1)
        if(this.this_first_day_of_week <= 0) {
            //「1日の曜日-1」が付け足す先月の日数。その日数の絶対値をマイナスにして、カウントアップし、0未満の場合は繰り返す。
            while(this.this_first_day_of_week < 0) {
                this.dateList.add(new DateList(this.this_year, this.this_month - 1, this.preMaxDate+this.this_first_day_of_week+1, false));
                this.this_first_day_of_week++;
            }
            this.idx++;
        }

        //★② その月の日付生成
        while(this.idx <= this.this_maxDate) {
            // 当日は当日フラグ = true
            if(this.this_year == 2024 && this.idx == this.this_date) {
                this.dateList.add(new DateList(this.this_year, this.this_month, this.idx, true));
                Log.d("11111====>", String.valueOf(this.idx));
                // 当日は当日フラグ = false
            } else {
                this.dateList.add(new DateList(this.this_year, this.this_month, this.idx, false));
                Log.d("22222====>", String.valueOf(this.idx));
            }
            this.idx++;
        }

        //★③ 来月の日付生成
        this.rest = 7 - this.this_last_day_of_week;
        this.idx = 1;
        // 先月の最終週の日数（rest） + 次の週の日数（7日）
        while (rest+7 > 0) {
            this.dateList.add(new DateList(this.this_year, this.this_month + 1, this.idx, false));
            this.idx++;
            this.rest--;
        }
    }

    public class DateList {
        private int year; // 年
        private int month;// 月
        private int date; // 日にち
        private boolean todayFlg; // 今日かどうかのフラグ

        DateList(int year,int month, int date, boolean todayFlg) {
            this.year = year;
            this.month = month;
            this.date = date;
            this.todayFlg = todayFlg;
        }

        public int getYear() {
            return this.year;
        }

        public int getMonth() {
            return this.month;
        }

        public int getDate() {
            return this.date;
        }
    }
}