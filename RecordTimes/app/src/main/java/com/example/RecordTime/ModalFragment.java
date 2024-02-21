package com.example.RecordTime;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ModalFragment extends Fragment {

    public ModalFragment() {
        // Required empty public constructor
    }

    public static ModalFragment newInstance() {
        ModalFragment fragment = new ModalFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_modal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        EditText editText = (EditText)view.findViewById(R.id.contents);

        // モーダルフラグメント上のバツボタン押下
        FloatingActionButton closeButton = (FloatingActionButton)view.findViewById(R.id.close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // モーダルフラグメントを外す
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .setReorderingAllowed(true)//トランザクションに関与するフラグメントの状態変更を最適化
                        .remove(fragmentManager.findFragmentByTag("modalFragment"))
                        .commit();
            }
        });

        // 開始ボタン押下
        Button startButton = view.findViewById(R.id.start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // インサート
                if(editText == null) Log.d("------->", "null-----");
                else Log.d("------->", editText.toString());
            }
        });
    }
}