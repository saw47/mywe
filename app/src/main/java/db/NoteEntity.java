package db;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "noteTable")
class NoteEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "number")
    int mNumber;
    @ColumnInfo(name = "textNote")
    String mTextNote;
    @Nullable
    @ColumnInfo(name = "deadLine")
    Long mDeadLine;
    @ColumnInfo(name = "delayReminder")
    int mDelayReminder;
    @ColumnInfo(name = "important")
    boolean mImportant;
}
