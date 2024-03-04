package hgsportfolio.example.RecordTime;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import hgsportfolio.example.RecordTime.R;

import hgsportfolio.example.RecordTime.Rooms.AppDatabase;
import hgsportfolio.example.RecordTime.Rooms.TimeTableDao;
import hgsportfolio.example.RecordTime.Rooms.TimeTableEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class DateFragment extends Fragment {

    LocalDate localDate;
    Adapter adapter = new Adapter();
    Handler mainThreadHandler = new Handler();
    List<TimeTableEntity> returnedTimeTableEntities = new ArrayList<>();
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

        // インサート・アップデート・デリート後の処理
        getActivity().getSupportFragmentManager().setFragmentResultListener(
                "closeModal",
                getActivity(),
                (@NonNull String requestKey, @NonNull Bundle result) -> {
                    returnedTimeTableEntities = new ArrayList<>();
                    // レコードを取得し直して、adapter.notifyDataSetChanged を依頼
                    setNewRec();
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

        // 〇年〇月〇日をセット
        setDateText(view);

        // RecyclerView をセット
        RecyclerView recyclerView = view.findViewById(R.id.time_table_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // 表示するレコードを取得・adapter に通知
        setNewRec();

        // ============ モーダル関連 ===============

        // プラスボタン押下：モーダルを開く
        FloatingActionButton rec_detail = (FloatingActionButton)view.findViewById(R.id.rec_detail);
        rec_detail.setOnClickListener(new OpenInsertModal());
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

    // モーダルを開くメソッド
    public class OpenInsertModal implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            getParentFragmentManager().setFragmentResult("popModalOnDate", null);
        }
    }

    // RecyclerView の Adapter
    class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        public class ViewHolder extends RecyclerView.ViewHolder {

            TimeTableEntity rec;
            TextView textView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                this.textView = itemView.findViewById(R.id.every_time_table);
                this.textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 日フラグメントを表示
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.activity_fragment_container, UpdateDeleteModalFragment.newInstance(rec), "UpdateModalFragment")
                                .addToBackStack("UpdateModalFragment")
                                .commit();
                    }
                });
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
            holder.rec = returnedTimeTableEntities.get(position);
            holder.textView.setText(formatDateTime(holder.rec.dateTime) + " ： " + holder.rec.title);
            if (holder.rec.isDone) holder.textView.setBackgroundColor(Color.rgb(224,224,224)); // grey_300
        }

        private String formatDateTime(LocalDateTime dateTime) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            if (dateTime != null) return dateTime.format(formatter);
            else return null;
        }

        @Override
        public int getItemCount() {
            return returnedTimeTableEntities.size();
        }
    }

    public void setNewRec() {
        HandlerThread handlerThread = new HandlerThread("Select");
        handlerThread.start();

        Handler handler = new Handler(handlerThread.getLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                returnedTimeTableEntities = new ArrayList<>();
                AppDatabase database = Room.databaseBuilder(getActivity().getApplicationContext(),
                        AppDatabase.class, "TimeTable").build();
                TimeTableDao timeTableDao = database.timeTableDao();
                returnedTimeTableEntities.addAll(timeTableDao.getLimitedRecByDate(localDate.atStartOfDay(), localDate.plusDays(1).atStartOfDay()));

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
}