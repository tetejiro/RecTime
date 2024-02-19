package com.example.RecordTime.Rooms;

import androidx.room.TypeConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Converters {

    @TypeConverter
    public static LocalDateTime fromDateTime(String value) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//        return value == null ? null : LocalDateTime.parse(value, formatter);
        return value == null ? null : LocalDateTime.parse(value);
    }

    @TypeConverter
    public static String toDateTime(LocalDateTime datetime) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//        return datetime == null ? null : datetime.format(formatter);
        return datetime == null ? null : datetime.toString();
    }
}