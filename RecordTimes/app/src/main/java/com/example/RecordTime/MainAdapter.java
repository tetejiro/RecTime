package com.example.RecordTime;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {

    private List<MainActivity.RowData> rowDataList;

    MainAdapter(List<MainActivity.RowData> rowDataList) {
        this.rowDataList = rowDataList;
    }

    /**
     * 一行分のデータ
     */
    static class MainViewHolder extends RecyclerView.ViewHolder {
        ImageView hogeImage;
        TextView hogeTitle;
        TextView hogeContents;

        MainViewHolder(@NonNull View itemView) {
            super(itemView);
            hogeImage = itemView.findViewById(R.id.hoge_image_view);
            hogeTitle = itemView.findViewById(R.id.hoge_title_text_view);
            hogeContents = itemView.findViewById(R.id.hoge_contents_text_view);
        }
    }

    /**
     * ViewHolder作るメソッド
     * 最初しか呼ばれない。
     * ここでViewHolderのlayoutファイルをインフレーとして生成したViewHolderをRecyclerViewに返す。
     */
    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new MainViewHolder(view);
    }

    /**
     * ViewHolderとRecyclerViewをバインドする
     * 一行のViewに対して共通でやりたい処理をここで書く。今回はテキストのセットしかしてないけど。
     */
    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        MainActivity.RowData rowData = this.rowDataList.get(position);
        holder.hogeTitle.setText(rowData.hogeTitle);
        holder.hogeContents.setText(rowData.hogeContents);
    }

    /**
     * リストの行数
     */
    @Override
    public int getItemCount() {
        return rowDataList.size();
    }
}

