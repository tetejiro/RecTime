package com.example.RecordTime;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MonthFragment extends Fragment {
    @Override
    //container: fragment が挿入される親
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        inflater.inflate(R.layout.month_fragment, container, false);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_container);

        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 7);
        recyclerView.setLayoutManager(layoutManager);

        // Adapter を生成して RecyclerView にセット
        RecyclerView.Adapter adapter = new RecyclerView_Adapter(EveryDateInfo.createDateRow()); // アダプターに繰り返す要素をセット
        recyclerView.setAdapter(adapter); // recycleView に adapter をセット

        // フラグメントのレイアウトをインフレート
        return rootView;
    }
}
