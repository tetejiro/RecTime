package com.example.RecordTime;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.room.Room;

import com.example.RecordTime.Rooms.AppDatabase;
import com.example.RecordTime.Rooms.TimeTableDao;
import com.example.RecordTime.Rooms.TimeTableEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class UpdateModalFragment extends Fragment {

    TimeTableEntity rec;
    View view;
    TextView update_time;
    LocalDate localDate;

    public static UpdateModalFragment newInstance(TimeTableEntity rec) {
        UpdateModalFragment fragment = new UpdateModalFragment();
        Bundle args = new Bundle();
        args.putSerializable("rec", rec);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            this.rec = (TimeTableEntity) getArguments().getSerializable("rec");
        }

        // 更新する際の値のデフォルト値をセット（更新する際の値はこれを使う）
        localDate = localDate.of(rec.dateTime.getYear(), rec.dateTime.getMonthValue(), rec.dateTime.getDayOfMonth());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_modal_update, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        this.view = view;

        // 初期値セット
        setDefaultValue(view);

        // カレンダータップ：新たな日付を localDate にセット
        CalendarView calendarView = view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new SetLocalDate());

        // バツボタン押下：モーダルを閉じる
        FloatingActionButton closeButton = view.findViewById(R.id.closeUpdateModal);
        closeButton.setOnClickListener((View v) -> getActivity().getSupportFragmentManager().popBackStack());

        // 時間を押下：TimePicker がポップアップされる
        TextView updateTimeText = view.findViewById(R.id.update_time);
        updateTimeText.setOnClickListener(new OpenTimePicker(view));

        // 更新ボタン：アップデート
        Button updateButton = view.findViewById(R.id.update_button);
        updateButton.setOnClickListener(new Update());
    }

    // =======================  utility  ======================== //

    // デフォルト値セット
    public void setDefaultValue(View view) {
        // カレンダー（日付）
        Calendar cl = Calendar.getInstance();
        cl.set(rec.dateTime.getYear(), rec.dateTime.getMonthValue() - 1, rec.dateTime.getDayOfMonth());
        CalendarView calendarView = view.findViewById(R.id.calendarView);
        calendarView.setDate(cl.getTimeInMillis()); // ミリ秒でのみ初期値セット可能

        // 時間
        update_time = view.findViewById(R.id.update_time);
        update_time.setText(twoDigit(rec.dateTime.getHour()) + ":" + twoDigit(rec.dateTime.getMinute()));

        // 完了チェック
        SwitchMaterial switchMaterial = (SwitchMaterial)view.findViewById(R.id.update_done);
        if (rec.isDone) switchMaterial.setChecked(true);

        // タイトル
        EditText editText = view.findViewById(R.id.update_title);
        editText.setText(rec.title);
    }

    // カレンダーの日付変更：localDate に値をセット
    class SetLocalDate implements CalendarView.OnDateChangeListener {
        @Override
        public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int date) {
            localDate = localDate.of(year, month, date);
        }
    }

    // 時間をタップ：TimePicker を開く
    class OpenTimePicker implements View.OnClickListener {
        View view;

        public OpenTimePicker(View view) {
            this.view = view;
        }

        @Override
        public void onClick(View view) {
            DialogFragment newFragment = new TimePickerFragment(rec.dateTime.getHour(), rec.dateTime.getMinute(), this.view);
            newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
        }
    }

    // TimePicker：デフォルト値セット・新しい値のセット時処理
    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        int hour;
        int minute;
        View view;

        public TimePickerFragment(int hour, int minute, View view) {
            this.hour = hour;
            this.minute = minute;
            this.view = view;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            TextView update_time = this.view.findViewById(R.id.update_time);
            update_time.setText(twoDigit(hourOfDay) + ":" + twoDigit(minute));
        }
    }

    // 更新ボタン押下：update
    class Update implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // 新しい値を rec にセット
            setRecNew();

            // アップデート → adapter に通知
            updateRec();

            // モーダル閉じる
            closeModal();
        }
    }

    // Rec に値をセット
    public void setRecNew() {
        // 時間取得　※ update_time(TextView) から取得
        TextView update_time = view.findViewById(R.id.update_time);
        String val = update_time.getText().toString();
        int setHour = Integer.parseInt(val.substring(0,2));
        int setMinute = Integer.parseInt(val.substring(3));
        LocalTime localTime = LocalTime.of(setHour, setMinute);
        Log.d("LocalTime=======>", String.valueOf(localTime));

        // 日付・時間
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        // 完了
        SwitchMaterial switchMaterial = view.findViewById(R.id.update_done);
        // タイトル
        TextView title = view.findViewById(R.id.update_title);
        String titleText = title.getText().toString();

        rec.dateTime = localDateTime;
        rec.title = titleText;
        rec.isDone = switchMaterial.isChecked();
    }

    // dao からアップデート
    public void updateRec() {
        // 別スレ生成 -> 開始
        HandlerThread handlerThread = new HandlerThread("Update");
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
                timeTableDao.update(rec);

                TimeTableEntity newRec = timeTableDao.getTargetRec(rec.id);

                // DateFragment へ通知 → adapter を更新
                getParentFragmentManager().setFragmentResult("closeModal", null);
            }
        });
    }

    // モーダル閉じる
    public void closeModal() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .remove(fragmentManager.findFragmentByTag("UpdateModalFragment"))
                .addToBackStack("UpdateModalFragment")
                .commit();
    }

    // 桁数をそろえる
    public static String twoDigit(int val) {
        String stringVal;
        if (val < 10) stringVal = "0" + String.valueOf(val);
        else stringVal = String.valueOf(val);
        return stringVal;
    }
}
