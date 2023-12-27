package com.example.RecordTime.Rooms;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TimeTable_Dao {

    @Insert
    void insert(TimeTable_RoomEntity timeTable_roomEntity);

    @Query("select * from TimeTable")
    List<TimeTable_RoomEntity> getAll();

    @Query("delete from TimeTable")
    void deleteAll();

    @Query("delete from TimeTable where id = :x")
    void delete(int x);
}
