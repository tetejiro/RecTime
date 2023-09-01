package com.example.RecordTime;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerView_Adapter extends RecyclerView.Adapter<RecyclerView_Adapter.ViewHolder>{

    private List<Dates> DateList;

    RecyclerView_Adapter(List<Dates> DateList) {
        this.DateList = DateList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateBox;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateBox = itemView.findViewById(R.id.every_date);
        }
    }

    @NonNull
    @Override
    // Inflate（複製）した ViewHolder(繰り返すレイアウト) を RecyclerViewに返す
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_viewholder, parent, false);
        return new ViewHolder(view);
    }

    // ViewHolderとRecyclerViewをバインドする(共通処理もここで記載する。)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Dates Dates = this.DateList.get(position);
        holder.dateBox.setText(position-1);
    }

    // 繰り返しの数
    @Override
    public int getItemCount() {
        return DateList.size();
    }
}
