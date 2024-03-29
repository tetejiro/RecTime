package hgsportfolio.example.RecordTime;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import hgsportfolio.example.RecordTime.Rooms.AppDatabase;
import hgsportfolio.example.RecordTime.Rooms.TimeTableDao;
import hgsportfolio.example.RecordTime.Rooms.TimeTableEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class UpdateDeleteModalFragment extends Fragment {

    TimeTableEntity rec;
    View view;
    TextView update_time;
    FrameLayout layout;

    public static UpdateDeleteModalFragment newInstance(TimeTableEntity rec) {
        UpdateDeleteModalFragment fragment = new UpdateDeleteModalFragment();
        Bundle args = new Bundle();
        args.putSerializable("rec", rec);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) this.rec = (TimeTableEntity) getArguments().getSerializable("rec");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_modal_update_delete, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        this.view = view;

        // 背景タップ：キーボードを下げる
        layout = (FrameLayout) view.findViewById(R.id.modal_update);
        layout.setOnTouchListener(new RemoveKeyboard());

        // 各初期値セット
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

        // 削除ボタン：delete
        Button deleteButton = view.findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new Delete());
    }

    // =======================  utility  ======================== //

    // キーボードを下げる
    class RemoveKeyboard implements View.OnTouchListener {
        @Override
        public boolean onTouch(android.view.View view, MotionEvent motionEvent) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(layout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            return false;
        }
    }

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
            LocalDate localDate = LocalDate.of(year, month + 1, date);
            LocalTime localTime = LocalTime.of(rec.getDateTime().getHour(), rec.getDateTime().getMinute());
            rec.setDateTime(LocalDateTime.of(localDate, localTime));
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
            DialogFragment newFragment = new TimePickerFragment(rec, this.view);
            newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
        }
    }

    // TimePicker：デフォルト値セット・新しい値のセット時処理
    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        TimeTableEntity rec;
        LocalDateTime localDateTime;
        View view;

        public TimePickerFragment(TimeTableEntity rec, View view) {
            this.rec = rec;
            this.localDateTime = rec.getDateTime();
            this.view = view;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, localDateTime.getHour(), localDateTime.getMinute(),
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // 時間変更後、TimePickerの初期値は変更された値：初期値である rec の LocalDateTime を選択された値に更新
            LocalDate date = LocalDate.of(localDateTime.getYear(), localDateTime.getMonthValue(), localDateTime.getDayOfMonth());
            rec.setDateTime(LocalDateTime.of(date, LocalTime.of(hourOfDay, minute)));

            // テキストとして表示
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
        }
    }

    // Rec に値をセット(update)
    public void setRecNew() {

        // 完了
        SwitchMaterial switchMaterial = view.findViewById(R.id.update_done);
        rec.setIsDone(switchMaterial.isChecked());
        // タイトル
        TextView title = view.findViewById(R.id.update_title);
        String titleText = title.getText().toString();
        rec.setTitle(titleText);

        // ※日付・時間は都度変更されている。
    }

    // dao (update)
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

                // モーダル閉じる
                getActivity().getSupportFragmentManager().popBackStack();

                // DateFragment へ通知 → adapter を更新
                getParentFragmentManager().setFragmentResult("closeModal", null);
            }
        });
    }

    // delete
    class Delete implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            // 別スレ生成 -> 開始
            HandlerThread handlerThread = new HandlerThread("Delete");
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
                    timeTableDao.delete(rec);

                    // モーダル閉じる
                    getActivity().getSupportFragmentManager().popBackStack();

                    // DateFragment へ通知 → adapter を更新
                    getParentFragmentManager().setFragmentResult("closeModal", null);
                }
            });
        }
    }

    // 桁数をそろえる
    public static String twoDigit(int val) {
        String stringVal;
        if (val < 10) stringVal = "0" + String.valueOf(val);
        else stringVal = String.valueOf(val);
        return stringVal;
    }
}
