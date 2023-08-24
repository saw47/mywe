package object;

import java.util.Objects;

public final class Note {

    private final int number;
    private final String textNote;
    private final Long deadLine;
    private final int delayReminder;
    private final boolean important;

    public int getNumber(){
        return number;
    }
    public String getTextNote() {
        return textNote;
    }
    public Long getDeadLine() {
        return deadLine;
    }
    public int getDelayReminder() {
        return delayReminder;
    }
    public boolean getImportant(){
        return important;
    }

    public static class Builder {
        private final String textNote;
        private int number = 0;

        private Long deadLine = 0L;
        private int delayReminder = 0;
        private boolean important = false;

        public Builder (String textNote)
        {
            this.textNote = textNote;
        }

        public Builder number(int val) {
            number = val;
            return this;
        }
        public Builder deadLine(long val) {
            deadLine = val;
            return this;
        }

        public Builder delayReminder(int val) {
            delayReminder = val;
            return this;
        }

        public Builder important(boolean val) {
            important = val;
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
        this.deadLine = builder.deadLine;
        this.delayReminder = builder.delayReminder;
        this.important = builder.important;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return number == note.number &&
               delayReminder == note.delayReminder &&
               important == note.important &&
               textNote.equals(note.textNote) &&
               deadLine.equals(note.deadLine);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, textNote, deadLine, delayReminder, important);
    }
}


