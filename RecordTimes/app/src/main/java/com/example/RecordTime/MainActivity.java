package com.example.RecordTime;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentResultListener;

import android.os.Bundle;

import java.time.LocalDate;


public class MainActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // メインアクティビティ → 月フラグメント
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)//トランザクションに関与するフラグメントの状態変更を最適化
                    .add(R.id.activity_fragment_container, MonthFragment.newInstance())
                    .addToBackStack("MonthFragment")
                    .commit();
        }

        // 月フラグメント → 日フラグメント（listener を設置して、受け取ったときに処理を行う）
        getSupportFragmentManager().setFragmentResultListener("monthToDate", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                LocalDate date = (LocalDate) result.getSerializable("date");

                // 日フラグメントを表示
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.activity_fragment_container, DateFragment.newInstance(date), "DateFragment")
                        .addToBackStack("DateFragment")
                        .commit();
            }
        });

        // 日フラグメント → モーダルを載せる。
        getSupportFragmentManager().setFragmentResultListener("popModalOnDate", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                String modalType = result.getString("modalType");

                // 日付フラグメントの上にモーダルフラグメントを置く
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("ModalFragment");
                if(fragment == null || !fragment.isVisible()) {
                        getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .add(R.id.activity_fragment_container, InsertModalFragment.newInstance(modalType), "ModalFragment")
                            .addToBackStack("ModalFragment")
                            .commit();
                }
            }
        });
    }
}