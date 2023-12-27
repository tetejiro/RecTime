package com.example.RecordTime;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.RecordTime.Rooms.TimeTable_RoomEntity;

import java.util.ArrayList;
import java.util.List;

public class Timely_RecyclerView_Adapter extends RecyclerView.Adapter<Timely_RecyclerView_Adapter.ViewHolder> {

    private final List<TimeTable_RoomEntity> itemList = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    void init() {
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    void load(List<TimeTable_RoomEntity> newItemList) {
//        itemList.clear();
//        itemList.addAll(newItemList); //防御コピー
//        // Recycler Viewは、表示するリストの中身を更新するためにこのメソッドの呼び出しが必要。
//        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(itemList.get(position).title);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        private ViewHolder(@NonNull ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false));
            // itemView: RecyclerView は itemView によりアクセスできる。
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}