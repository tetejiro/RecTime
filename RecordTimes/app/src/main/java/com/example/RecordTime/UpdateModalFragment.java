package com.example.RecordTime;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.RecordTime.Rooms.TimeTableEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class UpdateModalFragment extends Fragment {

    TimeTableEntity rec;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_modal_update, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // 初期値セット
        setDefaultValue(view);

        // バツボタン押下：モーダルを閉じる
        FloatingActionButton closeButton = view.findViewById(R.id.closeUpdateModal);
        closeButton.setOnClickListener(new CloseModal());

        // 時間を押下：TimePicker がポップアップされる
        TextView updateTimeText = view.findViewById(R.id.update_time);
        updateTimeText.setOnClickListener(new OpenTimePicker(view));
    }

    public void setDefaultValue(View view) {
        // カレンダー（日付）
        Calendar cl = Calendar.getInstance();
        cl.set(rec.datetime.getYear(), rec.datetime.getMonthValue() - 1, rec.datetime.getDayOfMonth());
        CalendarView calendarView = view.findViewById(R.id.calendarView);
        calendarView.setDate(cl.getTimeInMillis());

        // 時間
        TextView textView = view.findViewById(R.id.update_time);
        textView.setText(rec.datetime.getHour() + ":" + rec.datetime.getMinute());

        // タイトル
        EditText editText = view.findViewById(R.id.update_title);
        editText.setText(rec.title);
    }

    class OpenTimePicker implements View.OnClickListener {
        View view;

        public OpenTimePicker(View view) {
            this.view = view;
        }

        @Override
        public void onClick(View view) {
            DialogFragment newFragment = new TimePickerFragment(rec.datetime.getHour(), rec.datetime.getMinute(), this.view);
            newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
        }
    }

    class CloseModal implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            // モーダルを 1つ前にもどす
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }


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
            String StringHour;
            String StringMinutes;

            // 2桁にそろえる
            if (hourOfDay < 10) StringHour = "0" + String.valueOf(hourOfDay);
            else StringHour = String.valueOf(hourOfDay);
            if (minute < 10) StringMinutes = "0" + String.valueOf(minute);
            else StringMinutes = String.valueOf(minute);

            TextView textView = this.view.findViewById(R.id.update_time);
            textView.setText(StringHour + " : " + StringMinutes);
        }
    }
}
