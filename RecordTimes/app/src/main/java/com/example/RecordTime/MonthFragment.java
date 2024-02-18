package com.example.RecordTime;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@li MonthFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class MonthFragment extends Fragment {

    YearMonth yearMonth = YearMonth.now();
    View view;
    Adapter adapter = new Adapter();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @para param1 Parameter 1.
     * @para param2 Parameter 2.
     * @return A new instance of fragment MonthFragment.
     */
    // TODO: Rename and change types and number of parameters
//    public static MonthFragment newInstance(String param1, String param2) {
//        MonthFragment fragment = new MonthFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

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

        // 〇年・〇月をセット（setMonthOperable の中でも使用されている）
        setYearMonthText();

        // RecyclerView をセット
        RecyclerView recyclerView = view.findViewById(R.id.date_recycler_view_container);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 7));
        recyclerView.setAdapter(adapter);
    }

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

                    // 日付情報を渡す
                    Bundle result = new Bundle();
                    result.putInt("year", date.getYear());
                    result.putInt("month", date.getMonthValue());
                    result.putInt("date", date.getDayOfMonth());
                    getParentFragmentManager().setFragmentResult("date", result);

                    // 日付フラグメントとの交換
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)//トランザクションに関与するフラグメントの状態変更を最適化
                            .replace(R.id.activity_fragment_container, new DateFragment())
                            .addToBackStack("MonthFragment")
                            .commit();
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

            String[] week = {"日", "月", "火", "水", "木", "金", "土"};

            // 月の初日
            LocalDate localDate = LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), 1);

            // 月初日の曜日の数字
            int dayOfWeek = localDate.getDayOfWeek().getValue() == 7 ? 0 : localDate.getDayOfWeek().getValue();

            // 月の最終日
            int lastDate = localDate.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();

            // 曜日・日付の表示・色を付ける
            if(position < 7) {
                holder.dateButton.setText(week[position]);
                holder.dateButton.setTextColor(Color.BLACK);
                holder.dateButton.setBackgroundColor(Color.rgb(237,231,246));
            } else {
                // position - 7 - dayOfWeek：曜日の列(7) と 先月分(曜日分)からスタートする
                holder.date = localDate.plusDays(position - 7 - dayOfWeek);
                holder.dateButton.setText(String.valueOf(holder.date.getDayOfMonth()));
                holder.dayOfWeek = holder.date.getDayOfWeek().getValue();

                // 当日：緑
                if (holder.date.isEqual(LocalDate.now())) holder.dateButton.setBackgroundColor(Color.rgb(185,246,202));

                // 日曜：赤 ・ 土曜：青
                if (holder.dayOfWeek == 7) holder.dateButton.setTextColor(Color.RED);
                else if (holder.dayOfWeek == 6) holder.dateButton.setTextColor(Color.BLUE);

                // 先月：グレー
                if (position - 7 - dayOfWeek < 0) holder.dateButton.setTextColor(Color.GRAY);
                // 来月：グレー
                if (lastDate < (position + 1) - 7 - dayOfWeek) holder.dateButton.setTextColor(Color.GRAY);
            }
        }

        // 繰り返す回数
        @Override
        public int getItemCount() {
            return 7 + 7 * 6;
        }
    }
}