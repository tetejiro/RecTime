package com.example.RecordTime.Rooms;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;


@Entity(tableName = "time_table")
public class TimeTableEntity {

    @PrimaryKey
    public int id;

    @ColumnInfo(name = "title")
    @NonNull
    public String title;

    @ColumnInfo(name = "datetime")
    public LocalDateTime datetime;

    @ColumnInfo(name = "done")
    public Boolean done;

    public TimeTableEntity(String title) {
        this.title = title;
        setDatetime();
        this.done = false;
    }

    public void setTitle(String val) {
        this.title = val;
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
        return this.done;
    }

    public void setDone() {
        this.done = !isDone();
    }
}
