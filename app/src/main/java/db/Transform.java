package db;

import object.Note;

public class Transform {
    public static NoteEntity toEntity(Note note) {
        return new NoteEntity
                (note.getNumber(),
                 note.getTextNote(),
                 note.getIsSportNote(),
                 note.getNoteState()
                );
    }

    public static Note toNote(NoteEntity entity) {
        return new Note.Builder(entity.entityTextNote)
                .number(entity.entityNumber)
                .isSportNote(entity.entityIsSportNote)
                .noteState(entity.entityNoteState)
                .build();
    }
}
