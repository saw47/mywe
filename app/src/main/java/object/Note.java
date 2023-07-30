package object;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public final class Note {

    private int number = 0;
    private String textNote;
    private Long deadLine = 0L;
    private int delayReminder = 0;
    private boolean important = false;

    public Note(int number,String textNote, Long deadLine , int delayReminder, boolean important)
    {
        this.number = number;
        this.textNote = textNote;
        this.deadLine = deadLine;
        this.delayReminder = delayReminder;
        this.important = important;
    }


    public Note(String textNote, Long deadLine , int delayReminder, boolean important)
    {
        this.textNote = textNote;
        this.deadLine = deadLine;
        this.delayReminder = delayReminder;
        this.important = important;
    }

    public Note(String textNote)
    {
        this.textNote = textNote;
    }

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

}


