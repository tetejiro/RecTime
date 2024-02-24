package com.example.RecordTime;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;


public class MonthFragment extends Fragment {

    YearMonth yearMonth = YearMonth.now();
    View view;
    Adapter adapter = new Adapter();

    public static MonthFragment newInstance() {
        return new MonthFragment();
    }

    // Fragmentが作成されたときに起動・Fragmentが生存する間必要な処理を初期化
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // onCreate の後に呼び出され Viewを生成して返す
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_month, container, false);
    }

    // Viewが生成された後に呼び出し・Viewの初期化とFragmentの状態の復元
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        this.view = view;

        // 先月・来月への移動・それに関連する一連の処理・各日付の押下時の処理
        setButtonOperable();

        // 〇年・〇月をセット
        setYearMonthText();

        // RecyclerView をセット
        RecyclerView recyclerView = view.findViewById(R.id.date_recycler_view_container);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 7));
        recyclerView.setAdapter(adapter);
    }

    // << >> 押下時：月を1月ずつずらす・〇年〇月セット・adapterに通知
    public void setButtonOperable() {
        Button nextButton = view.findViewById(R.id.next);
        nextButton.setOnClickListener(view -> {
            yearMonth = yearMonth.plusMonths(1);

            setYearMonthText();

            adapter.notifyDataSetChanged();
        });

        Button prevButton = view.findViewById(R.id.prev);
        prevButton.setOnClickListener(view -> {
            yearMonth = yearMonth.minusMonths(1);

            setYearMonthText();

            adapter.notifyDataSetChanged();
        });
    }

    // 〇年〇月をセット
    public void setYearMonthText() {
        // 〇年
        TextView year_text = this.view.findViewById(R.id.year);
        year_text.setText(yearMonth.getYear() + "年");

        // 〇月
        TextView month_text = this.view.findViewById(R.id.month);
        month_text.setText(yearMonth.getMonthValue() + "月");
    }

    // =========================== Adapter ==============================
    class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        // =========================== ViewHolder start ==============================
        public class ViewHolder extends RecyclerView.ViewHolder {

            Button dateButton;

            LocalDate date;

            int dayOfWeek;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                this.dateButton = itemView.findViewById(R.id.every_date);

                // 日付押下時の処理
                dateButton.setOnClickListener(view -> {

                    // 日付フラグメントとの交換
                    Bundle args = new Bundle();
                    args.putSerializable("date", date);
                    getParentFragmentManager().setFragmentResult("monthToDate", args);
                });
            }
        }

        // =========================== ViewHolder end ================================

        // getItemCount で指定された回数分、inflate する。
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_date, parent, false);
            return new ViewHolder(view);
        }

        // ↑で返された View 1つ1つに対して行う処理を記載する。
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            // 月の初日
            LocalDate localDate = LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), 1);

            // 月初日の曜日の数字
            int dayOfWeek = localDate.getDayOfWeek().getValue() == 7 ? 0 : localDate.getDayOfWeek().getValue();

            // 月の最終日
            int lastDate = localDate.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();

            // 曜日・日付の表示・色を付ける
            // position - dayOfWeek 先月分(曜日分)からスタートする
            holder.date = localDate.plusDays(position - dayOfWeek);
            holder.dateButton.setText(String.valueOf(holder.date.getDayOfMonth()));
            holder.dayOfWeek = holder.date.getDayOfWeek().getValue();

            // 今日：背景ON
            if (holder.date.isEqual(LocalDate.now())) holder.dateButton.setBackgroundColor(Color.rgb(223,204,204));

            // 先月：グレー
            if (position - dayOfWeek < 0) holder.dateButton.setTextColor(Color.GRAY);
            // 来月：グレー
            if (lastDate < (position + 1) - dayOfWeek) holder.dateButton.setTextColor(Color.rgb(189,189,189));
        }

        // 繰り返す回数
        @Override
        public int getItemCount() {
            return 7 * 6;
        }
    }
}