package com.example.RecordTime;

import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 月画面フラグメントを表示
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)//トランザクションに関与するフラグメントの状態変更を最適化
                .add(R.id.month_fragment_container, new MonthFragment())
                .commit();
    }
}