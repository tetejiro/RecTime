package com.example.RecordTime.Rooms;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;


@Entity(tableName = "time_table")
public class TimeTableEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "title")
    @NonNull
    public String title;

    @ColumnInfo(name = "datetime")
    public LocalDateTime datetime;

    @ColumnInfo(name = "isDone")
    public Boolean isDone;

    public TimeTableEntity(String title, Boolean isDone) {
        this.title = title;
        setDatetime();
        this.isDone = isDone;
    }

    public String getTitle() {
        return this.title;
    }

    public LocalDateTime getDatetime() {
        return this.datetime;
    }

    public void setDatetime() {
        this.datetime = LocalDateTime.now();
    }

    public Boolean isDone() {
        return this.isDone;
    }

    public void setDone() {
        this.isDone = !isDone();
    }
}
