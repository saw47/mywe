package object;

import java.util.Objects;

import util.TabPositionState;

public final class Note {

    private final int number;
    private final String textNote;
    private final boolean isSportNote;
    private final TabPositionState noteState;

    public int getNumber(){
        return number;
    }
    public String getTextNote() {
        return textNote;
    }
    public boolean getIsSportNote() {
        return isSportNote;
    }
    public TabPositionState getNoteState() {
        return noteState;
    }

    public static class Builder {
        private final String textNote;
        private int number = 0;
        private boolean isSportNote = false;
        private TabPositionState noteState = TabPositionState.UNDEF;

        public Builder (String textNote)
        {
            this.textNote = textNote;
        }

        public Builder number(int val) {
            number = val;
            return this;
        }

        public Builder isSportNote(boolean val) {
            isSportNote = val;
            return this;
        }

        public Builder noteState(TabPositionState state) {
            noteState = state;
            return this;
        }

        public Note build() {
            return new Note(this);
        }
    }

    private Note(Builder builder)
    {
        this.number = builder.number;
        this.textNote = builder.textNote;
        this.isSportNote = builder.isSportNote;
        this.noteState = builder.noteState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return number == note.number &&
                textNote.equals(note.textNote) &&
                isSportNote == note.isSportNote &&
                noteState == note.noteState;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, textNote, isSportNote, noteState);
    }
}


