package com.example.RecordTime;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;


public class MainActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 月画面フラグメントを表示
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)//トランザクションに関与するフラグメントの状態変更を最適化
                    .add(R.id.activity_fragment_container, new MonthFragment())
                    .commit();
        }
    }
}