package db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

import object.Note;
import util.TabPositionState;


@Entity(tableName = "noteTable")
public class NoteEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "number")
    int entityNumber = 0;
    @ColumnInfo(name = "textNote")
    String entityTextNote;
    @ColumnInfo(name = "isSportNote")
    boolean entityIsSportNote;
    @ColumnInfo(name = "isActualNote")
    TabPositionState entityNoteState;

    NoteEntity(int entityNumber,
               String entityTextNote,
               boolean entityIsSportNote,
               TabPositionState entityNoteState) {

        this.entityNumber = entityNumber;
        this.entityTextNote = entityTextNote;
        this.entityIsSportNote = entityIsSportNote;
        this.entityNoteState = entityNoteState;
    }
}
