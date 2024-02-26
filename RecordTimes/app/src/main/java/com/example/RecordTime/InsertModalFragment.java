package com.example.RecordTime;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.room.Room;

import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.RecordTime.Rooms.AppDatabase;
import com.example.RecordTime.Rooms.TimeTableDao;
import com.example.RecordTime.Rooms.TimeTableEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDateTime;

public class InsertModalFragment extends Fragment {
    private final String ONLY_TIME_MODAL = "only_time";
    private final String BLANK_TITLE = "未入力";

    String modalType;

    View view;
    Boolean isDone;

    public InsertModalFragment() {
        // Required empty public constructor
    }

    public static InsertModalFragment newInstance(String val) {
        InsertModalFragment fragment = new InsertModalFragment();
        Bundle args = new Bundle();
        args.putString("modalType", val);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            modalType = getArguments().get("modalType").toString();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int resource;
        if (modalType.equals(ONLY_TIME_MODAL)) resource = R.layout.fragment_modal_only_time;
        else resource = R.layout.fragment_modal_detail;
        return inflater.inflate(resource, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.view = view;

        // only_time モーダル
        if(modalType.equals(ONLY_TIME_MODAL)) {
            // OKボタン押下；
            Button OK_button = view.findViewById(R.id.confirm_button);
            OK_button.setOnClickListener(new ChainOfInsert());
        // detail モーダル
        } else {
            // 開始ボタン押下：インサート・モーダル閉じる・adapterに通知
            Button startButton = view.findViewById(R.id.start);
            startButton.setOnClickListener(new ChainOfInsert());

            Button endButton = view.findViewById(R.id.end);
            endButton.setOnClickListener(new ChainOfInsert());
        }

        // モーダルフラグメント上のバツボタン押下：モーダル閉じる
        FloatingActionButton closeButton = (FloatingActionButton) view.findViewById(R.id.close);
        closeButton.setOnClickListener(new CloseModal());
    }

    /**
     *  インサート・モーダル閉じる・adapterに通知
     *  引数無し：初期値をインサート
     *  引数：引数をインサート
     */
    // テキストエディタから文字列を取得
    class ChainOfInsert implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            // ボタンの文字列が「終了」のみ：isDone を true にする。（デフォルトは true）
            Button button = (Button)view;
            isDone = button.getText().equals("終了");

            // 別スレ生成 -> 開始
            HandlerThread handlerThread = new HandlerThread("Insert");
            handlerThread.start();
            //作成したHandlerThread(別スレ)内部のLooperを引数として、HandlerThread(のLooper)にメッセージを送るHandlerを生成する。
            Handler handler = new Handler(handlerThread.getLooper());
            //Handlerのpostメソッドでメッセージ(タスク：重たい処理)を送信する。
            handler.post(new InsertTimeTable());


            // 日フラグメントへ通知して　adapter.notifyDataSetChanged
            moveToDate();
            // モーダルフラグメントを取り外す
            removeModal();
        }
    }

    // レコードをインサート
    class InsertTimeTable implements Runnable {
        String title = BLANK_TITLE;
        @Override
        public void run() {
            // 時間のみモーダル　かつ　EditText が "" でない：BLANK_TITLE
            EditText editText = (EditText) view.findViewById(R.id.contents);
            if(!modalType.equals(ONLY_TIME_MODAL) && !editText.getText().toString().equals("")) {
                title = editText.getText().toString();
            }
            AppDatabase database = Room.databaseBuilder(getActivity().getApplicationContext(),
                    AppDatabase.class, "TimeTable").build();
            TimeTableDao timeTableDao = database.timeTableDao();
            timeTableDao.insert(new TimeTableEntity(title, isDone));
        }
    }

    // モーダルの閉じるボタン
    class CloseModal implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            removeModal();
        }
    }

    // モーダルフラグメントを外す
    public void removeModal() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setReorderingAllowed(true)//トランザクションに関与するフラグメントの状態変更を最適化
                .remove(fragmentManager.findFragmentByTag("ModalFragment"))
                .commit();
    }

    // 日フラグメントに通知する。(adapter.notifyDataSetChanged() を依頼するため)
    public void moveToDate() {
        getParentFragmentManager().setFragmentResult("closeModal", null);
    }
}