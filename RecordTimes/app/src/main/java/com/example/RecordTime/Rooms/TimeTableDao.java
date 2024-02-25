package com.example.RecordTime.Rooms;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;

import com.example.RecordTime.Rooms.TimeTableEntity;

import java.time.LocalDate;
import java.util.List;

@Dao
public interface TimeTableDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TimeTableEntity timeTableEntity);

    @Query("SELECT id, title, is_done FROM time_tables")
    List<TimeTableEntity> getAll();

    @Query("DELETE FROM time_tables")
    public void deleteAll();
}
