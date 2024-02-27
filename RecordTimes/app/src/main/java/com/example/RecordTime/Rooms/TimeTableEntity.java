package com.example.RecordTime.Rooms;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.time.LocalDateTime;


@Entity(tableName = "time_tables")
public class TimeTableEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "title")
    @NonNull
    public String title;

    @ColumnInfo(name = "date_time")
    public LocalDateTime dateTime;

    @ColumnInfo(name = "is_done")
    public Boolean isDone;

    public TimeTableEntity(String title, LocalDateTime dateTime, Boolean isDone) {
        this.title = title;
        this.dateTime = dateTime;
        this.isDone = isDone;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIsDone(Boolean isDone) {
        this.isDone = isDone;
    }
}
