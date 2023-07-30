package listener;

import object.Note;

public interface NoteCardClickListener {
    public void onFrameClick(Note note);
    public void onFrameLongClick(Note note);
}
