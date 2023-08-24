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
        return new Note.Builder(entity.entityTextNote)
                .number(entity.entityNumber)
                .deadLine(entity.entityDeadLine)
                .delayReminder(entity.entityDelayReminder)
                .important(entity.entityImportant)
                .build();
    }
}
