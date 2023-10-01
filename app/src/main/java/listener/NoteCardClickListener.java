package listener;
import java.util.List;
import object.Note;
import util.TabPositionState;

public interface NoteCardClickListener {
    void onFrameClick(Note note);
    void onFrameLongClick(Note note);
    boolean noteIsSelectedState();
    List<Note> getTempSelectedNotes();
    void unselectNote(Note note);
    void changeActualNoteState(Note note);
    TabPositionState getTabState();

}
