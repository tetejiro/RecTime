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
    private final String mode1 = "only_time";

    String modalMode;

    View view;

    public ModalFragment() {
        // Required empty public constructor
    }

    public static ModalFragment newInstance(String val) {
        ModalFragment fragment = new ModalFragment();
        Bundle args = new Bundle();
        args.putString("modalMode", val);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) modalMode = getArguments().get("modalMode").toString();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int resource;
        if (modalMode.equals(mode1)) resource = R.layout.fragment_modal_only_time;
        else resource = R.layout.fragment_modal_detail;
        return inflater.inflate(resource, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.view = view;

        if(modalMode.equals(mode1)) {
            //
        } else {
            // 開始ボタン押下
            Button startButton = view.findViewById(R.id.start);
            startButton.setOnClickListener(new ClickStart());
        }

        // モーダルフラグメント上のバツボタン押下
        FloatingActionButton closeButton = (FloatingActionButton) view.findViewById(R.id.close);
        closeButton.setOnClickListener(new CloseModal());
    }

    // モーダルの閉じるボタン
    class CloseModal implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            // モーダルフラグメントを外す
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .setReorderingAllowed(true)//トランザクションに関与するフラグメントの状態変更を最適化
                    .remove(fragmentManager.findFragmentByTag("ModalFragment"))
                    .commit();
        }
    }

    // 開始ボタン押下時に、テキストエディタから文字列を取得して TimeTable にインサートする
    class ClickStart implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            EditText editText = (EditText)view.findViewById(R.id.contents);
            if(editText == null) Log.d("------->", "null-----");
            else Log.d("------->", editText.toString());
        }
    }
}