package com.example.RecordTime.Rooms;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.RecordTime.Rooms.TimeTableEntity;

import java.util.List;

@Dao
public interface TimeTableDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TimeTableEntity timeTableEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<TimeTableEntity> timeTableEntities);

    @Query("SELECT * FROM time_table")
    List<TimeTableEntity> getAll();

    @Query("DELETE FROM time_table")
    public void deleteAll();
}
