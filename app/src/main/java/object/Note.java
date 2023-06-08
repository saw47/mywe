package object;

public final class Note {

    private final int number;
    private final String textNote;
    private final Long deadLine;
    private final int delayReminder;
    private final boolean important;

    public Note(int number, String textNote, Long deadLine, int delayReminder, boolean important)
    {
        this.number = number;
        this.textNote = textNote;
        this.deadLine = deadLine;
        this.delayReminder = delayReminder;
        this.important = important;
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


