package com.example.RecordTime.Rooms;

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

    @ColumnInfo(name = "datetime")
    public LocalDateTime datetime;

    @ColumnInfo(name = "is_done")
    public Boolean isDone;

    public TimeTableEntity(String title, Boolean isDone) {
        this.title = title;
        this.datetime = LocalDateTime.now();
        this.isDone = isDone;
    }

    public String getTitle() {
        return this.title;
    }

    public LocalDateTime getDatetime() {
        return this.datetime;
    }

    public void setDateTime() {
        this.datetime = LocalDateTime.now();
    }

    public Boolean isDone() {
        return this.isDone;
    }

    public void setDone() {
        this.isDone = !isDone();
    }
}
