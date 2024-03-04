package hgsportfolio.example.RecordTime.Rooms;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(
        version = 1,
        entities = {TimeTableEntity.class},
        exportSchema = false
)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract TimeTableDao timeTableDao();
}
