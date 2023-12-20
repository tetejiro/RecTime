package com.example.RecordTime.Room;

import androidx.room.RoomDatabase;

@androidx.room.Database(entities = {TimeTable_RoomEntity.class}, version = 1, exportSchema = false)
public abstract class Database extends RoomDatabase {
    public abstract TimeTable_Dao timeTable_dao();
}
