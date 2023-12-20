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
        tx.setText(month.toString() + "月");

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
        this.dateLists = new ArrayList<DateList>(); // push する対象

        Calendar cl = new GregorianCalendar();
        int maxDate = cl.getActualMaximum(Calendar.DATE); // 今月の最大日数
        int year = cl.get(Calendar.YEAR);
        int month = cl.get(Calendar.MONTH);

        int idx = 1;
        // 日数分のクラスを作って、配列に add する。
        while (idx <= maxDate) {
            DateList d = new DateList(year, month, idx);
            this.dateLists.add(d);
            idx++;
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
