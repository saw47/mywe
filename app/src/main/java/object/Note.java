package object;

import android.os.Bundle;

import java.util.Date;

public class Note {

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

    private Note(Builder builder)
    {
        this.number = builder.number;
        this.textNote = builder.textNote;
        this.deadLine = builder.deadLine;
        this.important = builder.important;
        this.delayReminder = builder.delayReminder;
    }


    public static class Builder {
        private final int number;
        private final String textNote;
        private Long deadLine;
        private int delayReminder;
        private boolean important;

        public Builder(String textNote)
        {
            this.number = -1;
            this.textNote = textNote;
            this.deadLine = null;
            this.important = false;
            this.delayReminder = -1;
        }

        public Builder deadLine(Long date) {
            this.deadLine = date;
            return this;
        }
        public Builder delayReminder(int delay) {
            this.delayReminder = delay;
            return this;
        }
        public Builder important(boolean important) {
            this.important = important;
            return this;
        }

        public Note build(Builder builder) {
            return new Note(this);
        }
    }
}


