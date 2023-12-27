package com.example.RecordTime.Rooms;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "TimeTable")
public class TimeTable_RoomEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String title;

    public TimeTable_RoomEntity(String title) {
        this.title = title;
    }

    public int getId() {
        return this.id;
    }
}
