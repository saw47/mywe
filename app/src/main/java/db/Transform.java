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
        return new Note(entity.entityNumber,
                        entity.entityTextNote,
                        entity.entityDeadLine,
                        entity.entityDelayReminder,
                        entity.entityImportant
        );
    }
}
