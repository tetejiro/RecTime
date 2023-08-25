package com.example.RecordTime;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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

        MainViewHolder(@NonNull View itemView) {
            super(itemView);
            hogeImage = itemView.findViewById(R.id.hoge_image_view);
        }
    }

    /**
     * ViewHolder作るメソッド
     * ------ 繰り返す要素１つ１つを作成して、作成した要素を返す役割 ------
     * 最初しか呼ばれない。
     * ここでViewHolderのlayoutファイルをインフレーとして生成したViewHolderをRecyclerViewに返す。
     */
    @NonNull
    @Override
    // 新しいViewHolderを作成する
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater.from: 指定されたコンテキストから LayoutInflater を取得します。 LayoutInflater 指定したxmlのレイアウト(View)リソースを利用できる仕組みhttps://qiita.com/Bth0061/items/c4f66477979d064913e4
        // inflate: 指定された XML ノードから新しいビュー階層を作る
        // int: ロードする XML レイアウト リソースの ID
        // ViewGroup: ( attachToRootが true の場合)生成された階層の親となるオプションのビュー 、
        // ( attachToRootが false の場合)返された階層のルートに LayoutParams 値のセットを提供する単純なオブジェクト 。

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new MainViewHolder(view);
    }

    /**
     * ViewHolderとRecyclerViewをバインドする
     * 一行のViewに対して共通でやりたい処理をここで書く。今回はテキストのセットしかしてないけど。
     * 引数：(holder)作成したビューホルダー・(position)ビューホルダーが表示される位置
     */
    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        MainActivity.RowData rowData = this.rowDataList.get(position);
    }

    /**
     * リストの行数
     */
    @Override
    public int getItemCount() {
        return rowDataList.size();
    }
}

