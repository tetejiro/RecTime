package com.example.RecordTime;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.room.Room;

import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

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
            OK_button.setOnClickListener(new InsertMethod());

        // detail モーダル
        } else {
            // 開始ボタン押下：インサート・モーダル閉じる・adapterに通知
            Button startButton = view.findViewById(R.id.start);
            startButton.setOnClickListener(new InsertMethod());

            // 削除ボタン押下：delete・モーダル閉じる・adapterに通知
            Button endButton = view.findViewById(R.id.end);
            endButton.setOnClickListener(new InsertMethod());

            // レイアウトタップ：キーボードを下げる
            View layout = (FrameLayout)view.findViewById(R.id.fragment_modal_detail);
            layout.setOnTouchListener(new SetKeyboardDown(layout));
            View containerView = (FragmentContainerView)getActivity().findViewById(R.id.activity_fragment_container);
            containerView.setOnTouchListener(new SetKeyboardDown(containerView));
        }

        // モーダルフラグメント上のバツボタン押下：モーダル閉じる
        FloatingActionButton closeButton = (FloatingActionButton) view.findViewById(R.id.close);
        closeButton.setOnClickListener((View v) -> removeModal());
    }

    /**
     *  インサート・モーダル閉じる・adapterに通知
     *  引数無し：初期値をインサート
     *  引数：引数をインサート
     */
    class InsertMethod implements View.OnClickListener {
        @Override
        public void onClick(View startButton) {
            // インサートするレコードに値をセット
            TimeTableEntity newRec = setNewVal(startButton);

            // インサート処理　→　モーダル閉じる
            insertRec(newRec);
        }
    }

    // インサートする値をセット
    public TimeTableEntity setNewVal(View startButton) {
        TimeTableEntity newRec = new TimeTableEntity(BLANK_TITLE, LocalDateTime.now(), false);

        // ボタンの文字列が「終了」：isDone = true
        Button button = (Button)startButton;
        newRec.setIsDone(button.getText().equals("終了"));

        // 時間のみモーダル　かつ　EditText が "" でない：BLANK_TITLE
        EditText editText = (EditText) view.findViewById(R.id.contents);
        if(!modalType.equals(ONLY_TIME_MODAL) && !editText.getText().toString().equals("")) {
            newRec.setTitle(editText.getText().toString());
        }
        return newRec;
    }

    // インサート処理
    public void insertRec(TimeTableEntity newRec) {
        // 別スレ生成 -> 開始
        HandlerThread handlerThread = new HandlerThread("Insert");
        handlerThread.start();
        //作成したHandlerThread(別スレ)内部のLooperを引数として、HandlerThread(のLooper)にメッセージを送るHandlerを生成する。
        Handler handler = new Handler(handlerThread.getLooper());
        //Handlerのpostメソッドでメッセージ(タスク：重たい処理)を送信する。
        handler.post(new Runnable() {
            @Override
            public void run() {
                AppDatabase database = Room.databaseBuilder(getActivity().getApplicationContext(),
                        AppDatabase.class, "TimeTable").build();
                TimeTableDao timeTableDao = database.timeTableDao();
                timeTableDao.insert(newRec);

                // 日フラグメントへ通知して（日フラグメントで adapter.notifyDataSetChanged ）
                getParentFragmentManager().setFragmentResult("closeModal", null);

                // モーダルを閉じる
                removeModal();
            }
        });
    }

    // モーダルフラグメントを外す
    public void removeModal() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    // キーボードを下げる
    class SetKeyboardDown implements View.OnTouchListener {
        View layout;
        public SetKeyboardDown(View layout) {
            this.layout = layout;
        }
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(layout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            return false;
        }
    }
}