package com.example.RecordTime;

import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MonthFragment extends Fragment {

    private ArrayList<DateList> dateLists;

    @Override
    //container: fragment が挿入される親
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.month_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onStart();

        dataInitialize();

        // 〇月を今月にする
        Integer month = this.dateLists.get(0).getMonth() + 1;
        TextView tx = view.findViewById(R.id.month);
        tx.setText(month + "月");

        // RecyclerView を生成して各日にちを生成
        RecyclerView recyclerView = view.findViewById(R.id.date_recycler_view_container);
        recyclerView.setHasFixedSize(true);
        // ①recycleView に layoutManager をセット
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 7));
        // ②Adapter を生成して RecyclerView にセット
        recyclerView.setAdapter(new Monthly_RecyclerView_Adapter(getActivity(), this.dateLists));
    }

    // dateLists = [DateList, , ...] を作成
    private void dataInitialize() {
        this.dateLists = new ArrayList<>(); // push する対象

        Calendar cl = new GregorianCalendar();
        int maxDate = cl.getActualMaximum(Calendar.DATE); // 今月の最大日数
        int year = cl.get(Calendar.YEAR);
        int month = cl.get(Calendar.MONTH);

        // 今月1日のインスタンス
        Calendar today = new GregorianCalendar(year, month, 1);
        // 今月1日の曜日
        int first_day_of_week = today.get(Calendar.DAY_OF_WEEK);
        // 今月最終日の曜日
        int last_day_of_week = new GregorianCalendar(year, month, maxDate).get(Calendar.DAY_OF_WEEK);
        // 先月の最大日数
        int preMaxDate = new GregorianCalendar(year, month-1, 1)
                                    .getActualMaximum(Calendar.DATE);

        int[] weekDays = {0, 0, 1, 2, 3, 4, 5, 6};

        // 日数分(とその前後)の日付クラスを作って、配列に add する。
        this.dateLists = new ArrayList<>();
        int idx = 0;
        // （マイナス）1日よりも前に表示する日数
        int before_first_day = - weekDays[first_day_of_week];

        // 1日よりも前の日付生成
        if(before_first_day <= 0) {
            while(before_first_day < 0) {
                this.dateLists.add(new DateList(year, month - 1, preMaxDate + before_first_day + 1));
                before_first_day++;
            }
            idx++;
        }
        // その月の日付生成
        while(idx <= maxDate) {
            this.dateLists.add(new DateList(year, month, idx));
            idx++;
        }
        // 来月の日付生成
        int rest = weekDays[last_day_of_week] + 1;
        idx = 1;
        // 先月の最終週との合計　14日分の日付を生成する。
        while (14 - rest > 0) {
            this.dateLists.add(new DateList(year, month + 1, idx));
            idx++;
            rest++;
        }
    }

    public class DateList {
        private int year; // 年
        private int month;// 月
        private int date; // 日にち

        DateList(int year,int month, int date) {
            this.year = year;
            this.month = month;
            this.date = date;
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
