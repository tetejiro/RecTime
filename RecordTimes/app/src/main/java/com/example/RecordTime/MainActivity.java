package com.example.RecordTime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.main_recycler_view);

        // RecyclerViewのレイアウトサイズを変更しない設定をONにする
        // パフォーマンス向上のための設定。
        recyclerView.setHasFixedSize(true);

        // RecyclerViewにlayoutManagerをセットする。
        // このlayoutManagerの種類によって「1列のリスト」なのか「２列のリスト」とかが選べる。
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 7);
        recyclerView.setLayoutManager(layoutManager);

        // Adapter生成してRecyclerViewにセット
        RecyclerView.Adapter mainAdapter = new MainAdapter(createRowData()); // アダプターに繰り返す要素をセット
        recyclerView.setAdapter(mainAdapter); // recycleView に adapter をセット
    }

    private List<RowData> createRowData() {
        List<RowData> dataSet = new ArrayList<>();
        Calendar cl = new GregorianCalendar();
        int maxDate = cl.getActualMaximum(Calendar.DATE);
        int idx = 1;
        while (idx <= maxDate) {
            RowData data = new RowData();

            dataSet.add(data);
            idx = idx + 1;
        }
        return dataSet;
    }

    class RowData {
        Image hogeImage;
    }
}
