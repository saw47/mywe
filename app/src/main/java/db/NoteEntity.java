package db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

import object.Note;


@Entity(tableName = "noteTable")
public class NoteEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "number")
    int entityNumber = 0;
    @ColumnInfo(name = "textNote")
    String entityTextNote;
    @ColumnInfo(name = "deadLine")
    Long entityDeadLine;
    @ColumnInfo(name = "delayReminder")
    int entityDelayReminder;
    @ColumnInfo(name = "important")
    boolean entityImportant;

    NoteEntity(int entityNumber,
               String entityTextNote,
               Long entityDeadLine,
               int entityDelayReminder,
               boolean entityImportant) {

        this.entityNumber = entityNumber;
        this.entityTextNote = entityTextNote;
        this.entityDeadLine = entityDeadLine;
        this.entityDelayReminder = entityDelayReminder;
        this.entityImportant = entityImportant;
    }
}
