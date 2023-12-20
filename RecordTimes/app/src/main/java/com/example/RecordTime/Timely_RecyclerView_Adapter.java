package com.example.RecordTime;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.RecordTime.Room.TimeTable_RoomEntity;

import java.util.List;

public class Timely_RecyclerView_Adapter extends RecyclerView.Adapter<Timely_RecyclerView_Adapter.ViewHolder> {


    //    ============= Adapter ===============
    List<TimeTable_RoomEntity> allTimeTable;
    Context context;

    Timely_RecyclerView_Adapter(Context context, List<TimeTable_RoomEntity> allTimeTable) {
        this.context = context;
        this.allTimeTable = allTimeTable;
    }

    @Override
    public int getItemCount() {
        return this.allTimeTable.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText("this.allTimeTable.get(position).title");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.time_viewholder, parent, false);
        return new Timely_RecyclerView_Adapter.ViewHolder(view);
    }
    //    ============= Adapter ===============


    //    ============= viewHolder ============
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.time_view_holder);
        }
    }
    //    ============= viewHolder =============
}

