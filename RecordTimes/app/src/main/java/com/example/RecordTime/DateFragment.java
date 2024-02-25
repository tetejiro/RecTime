package com.example.RecordTime;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
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

    LocalDate localDate;
    RecyclerView recyclerView;
    Adapter adapter = new Adapter();
    Handler mainThreadHandler = new Handler();

    List<TimeTableEntity> returnedTimeTableEntities = new ArrayList<>();

    TimeTableDao timeTableDao;

    InputMethodManager inputMethodManager;

    public static DateFragment newInstance(LocalDate localDate) {
        DateFragment fragment = new DateFragment();
        Bundle args = new Bundle();
        args.putSerializable("date", localDate);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        // 〇年・〇月・〇日を取得
        if (getArguments() != null) localDate = (LocalDate) getArguments().getSerializable("date");

        // モーダルを閉じたら、ここに戻ってくる。
        getActivity().getSupportFragmentManager().setFragmentResultListener("closeModal", getActivity(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {

                // スレッド作成し、select して returnedTimeTableEntities を再取得
                returnedTimeTableEntities.clear();
                HandlerThread handlerThread = new HandlerThread("Select");
                handlerThread.start();

                Handler handler = new Handler(handlerThread.getLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        AppDatabase database = Room.databaseBuilder(getActivity().getApplicationContext(),
                                AppDatabase.class, "TimeTable").build();
                        TimeTableDao timeTableDao = database.timeTableDao();
                        returnedTimeTableEntities.addAll(timeTableDao.getLimitByDate(localDate.atStartOfDay(), localDate.plusDays(1).atStartOfDay()));

                        // 「メインスレッド」に adapter.notifyDataSetChanged() を依頼する。
                        mainThreadHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_date, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        this.recyclerView = view.findViewById(R.id.time_table_recycler_view);

        // 〇年〇月〇日をセット
        setDateText(view);

        // 表示するレコードを取得する
        Thread thread = new Thread(new SelectTimeTableRec(localDate));
        thread.start();

        try {
            thread.join();
            // RecyclerView をセット
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // ============ モーダル関連 ===============

        // 時計マーク押下：モーダルを開く
        FloatingActionButton rec_only_time = (FloatingActionButton)view.findViewById(R.id.rec_only_time);
        rec_only_time.setOnClickListener(new OpenModal("only_time"));
        // プラスボタン押下：モーダルを開く
        FloatingActionButton rec_detail = (FloatingActionButton)view.findViewById(R.id.rec_detail);
        rec_detail.setOnClickListener(new OpenModal("detail"));
        // recyclerView押下：キーボードを閉じる
        recyclerView.setOnTouchListener(new CloseKeyboard());

        // TODO: 背景タップ時、モーダル非表示（キーワード表示されている場合キーボードのみ非表示）
    }


    // ======================= 切り出したメソッド・クラス ==========================

    // 1番上にある日付を表示するメソッド
    public void setDateText(View view) {

        // 〇年をセット
        TextView year_text = view.findViewById(R.id.selected_year);
        year_text.setText(localDate.getYear() + " 年");

        // 〇月をセット
        TextView month_text = view.findViewById(R.id.selected_month);
        month_text.setText(localDate.getMonthValue() + " 月");

        // 〇日をセット
        TextView date_text = view.findViewById(R.id.selected_date);
        date_text.setText(localDate.getDayOfMonth() + " 日");
    }

    // TimeTable レコードを取得（RecyclerView に渡す）
    public class SelectTimeTableRec implements Runnable {
        LocalDate localDate;
        SelectTimeTableRec(LocalDate localDate) {
            this.localDate = localDate;
        }
        @Override
        public void run() {
            returnedTimeTableEntities.clear();
            AppDatabase database = Room.databaseBuilder(getActivity().getApplicationContext(),
                    AppDatabase.class, "TimeTable").build();
            timeTableDao = database.timeTableDao();
            returnedTimeTableEntities.addAll(timeTableDao.getLimitByDate(localDate.atStartOfDay(), localDate.plusDays(1).atStartOfDay()));
        }
    }

    // モーダルを開くメソッド
    public class OpenModal implements View.OnClickListener {
        String modalType;
        public OpenModal(String val) {
            this.modalType = val;
        }

        @Override
        public void onClick(View view) {
            Bundle args = new Bundle();
            args.putString("modalType", modalType);
            args.putSerializable("date", localDate);
            getParentFragmentManager().setFragmentResult("popModalOnDate", args);
        }
    }

    // キーボードを非表示にするイベントリスナー（onTouch）
    public class CloseKeyboard implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            //キーボードを非表示にする
            inputMethodManager.hideSoftInputFromWindow(recyclerView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            return false;
        }
    }

    // RecyclerView の Adapter
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
            TimeTableEntity rec = returnedTimeTableEntities.get(position);
            holder.textView.setText(String.valueOf(rec.id) + "/" + rec.title + " / " + formatDateTime(rec.datetime));
            if (returnedTimeTableEntities.get(position).isDone) holder.textView.setBackgroundColor(Color.rgb(124,252,0)); // 赤
            else holder.textView.setBackgroundColor(Color.rgb(249,247,57)); // 黄色
        }

        private String formatDateTime(LocalDateTime date) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            return date.format(formatter);
        }

        @Override
        public int getItemCount() {
            return returnedTimeTableEntities.size();
        }
    }
}