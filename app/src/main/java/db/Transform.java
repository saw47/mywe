package db;

import object.Note;

public class Transform {
    public static NoteEntity toEntity(Note note) {
        return new NoteEntity
                (note.getNumber(),
                 note.getTextNote(),
                 note.getDeadLine(),
                 note.getDelayReminder(),
                 note.getImportant()
                );
    }

    public static Note toNote(NoteEntity entity) {
        return new Note(entity.mNumber,
                        entity.mTextNote,
                        entity.mDeadLine,
                        entity.mDelayReminder,
                        entity.mImportant
        );
    }
}
