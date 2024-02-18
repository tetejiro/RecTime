package com.example.RecordTime.Rooms;

import androidx.room.TypeConverter;

import java.time.LocalDateTime;

public class Converters {
    @TypeConverter
    public static LocalDateTime fromDateTime(String value) {
        return value == null ? null : LocalDateTime.parse(value);
    }

    @TypeConverter
    public static String toDateTime(LocalDateTime datetime) {
        return datetime == null ? null : datetime.toString();
    }
}