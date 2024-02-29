package com.example.RecordTime.Rooms;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.time.LocalDateTime;
import java.util.List;

@Dao
public interface TimeTableDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TimeTableEntity timeTableEntity);

    @Query("SELECT id, title, date_time, is_done FROM time_tables WHERE :start <= date_time AND date_time < :end ORDER BY date_time ASC")
    List<TimeTableEntity> getLimitedRecByDate(LocalDateTime start, LocalDateTime end);

    @Update
    void update(TimeTableEntity timeTableEntity);

    @Delete
    void delete(TimeTableEntity timeTableEntity);
}
