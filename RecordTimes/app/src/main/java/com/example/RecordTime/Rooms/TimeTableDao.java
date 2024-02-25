package com.example.RecordTime.Rooms;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.time.LocalDateTime;
import java.util.List;

@Dao
public interface TimeTableDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TimeTableEntity timeTableEntity);

    @Query("SELECT id, title, is_done FROM time_tables WHERE :start <= datetime AND datetime < :end")
    List<TimeTableEntity> getLimitByDate(LocalDateTime start, LocalDateTime end);
}
