package com.example.RecordTime;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class RecyclerView_Adapter extends RecyclerView.Adapter<RecyclerView_Adapter.ViewHolder>{

    Context content;
    ArrayList<MonthFragment.DateList> DateList;

    // ==================== Adapter ==============================
    RecyclerView_Adapter(Context content, ArrayList<MonthFragment.DateList> DateList) {
        this.content = content;
        this.DateList = DateList;
    }

    // 繰り返す回数を return する
    @Override
    public int getItemCount() {
        return this.DateList.size();
    }

    @NonNull
    @Override
    // getItemCount で指定した回数、ViewHolder を作成する。
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // content は MainActivity
        View view = LayoutInflater.from(content).inflate(R.layout.date_viewholder, parent, false);
        return new ViewHolder(view);
    }

    // ↑ onCreateViewHolder で作成された ViewHolder を引数にとり、その要素に対して処理をする。
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // 各日付の曜日を取得
        Integer year = this.DateList.get(position).getYear();
        Integer month = this.DateList.get(position).getMonth();
        Integer date = this.DateList.get(position).getDate();

        Calendar cl = new GregorianCalendar(year, month, date);
        Integer dayOfWeekNum = cl.get(Calendar.DAY_OF_WEEK);

        String[] weekDays = {"", "日", "月", "火", "水", "木", "金", "土"};
        String dayOfWeek = weekDays[dayOfWeekNum];

        // 日付 + 曜日を出力
        holder.dateBox.setText(date.toString() + dayOfWeek);

        holder.dateBox.setClickable(true);
        holder.dateBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView = (TextView)view;
                FragmentActivity fragmentActivity = (FragmentActivity)view.getContext();
                DateFragment dateFragment = new DateFragment();

                // 日付画面フラグメントにパラメータを付与する(〇月・〇日)
                Bundle bundle = new Bundle();
                TextView monthView = fragmentActivity.findViewById(R.id.month);
                bundle.putString("month", monthView.getText().toString());
                bundle.putString("date", textView.getText().toString());
                dateFragment.setArguments(bundle);

                // 日付画面フラグメントを起動する
                fragmentActivity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.month_fragment_container, dateFragment)
                        .addToBackStack("date")
                        .commit();
            }
        });
    }
    // ==================== Adapter ==============================



    // ==================== ViewHolder ===========================
    // RecyclerViewにいれるアイテム(一つ分)のガワを定義。
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateBox;

        // itemView は inflate されてできたもの
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateBox = itemView.findViewById(R.id.every_date);
        }
    }
    // ==================== ViewHolder ===========================

}
