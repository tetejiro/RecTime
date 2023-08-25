package com.example.RecordTime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.os.Bundle;

import java.util.ArrayList;
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
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Adapter生成してRecyclerViewにセット
        RecyclerView.Adapter mainAdapter = new MainAdapter(createRowData());
        recyclerView.setAdapter(mainAdapter);
    }

    private List<RowData> createRowData() {
        List<RowData> dataSet = new ArrayList<>();
        int i = 0;
        while (i < 20) {
            RowData data = new RowData();

            data.hogeTitle = "HogeTitle!!";
            data.hogeContents = "HogeHogeHogeHogeHogeHogeHogeHogeHogeHogeHoge";

            dataSet.add(data);
            i = i + 1;
        }
        return dataSet;
    }

    class RowData {
        Image hogeImage;
        String hogeTitle;
        String hogeContents;
    }
}
