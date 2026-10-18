package viewmodel;


import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import db.NoteRepository;
import listener.NoteCardClickListener;
import object.Note;
import util.SingleLiveEvent;
import util.TabPositionState;

public class ViewModelMain extends AndroidViewModel implements NoteCardClickListener {

    private final NoteRepository repository;

    private final List<Note> tempSelectedNotes = new ArrayList<>();
    private final MutableLiveData<TabPositionState> tabState = new MutableLiveData<>();
    private final MutableLiveData<SingleLiveEvent<Note>> frameClicked = new MutableLiveData<>();
    private final MutableLiveData<Boolean> noteIsSelected = new MutableLiveData<>();
    private final MutableLiveData<Boolean> noteIsDeleted = new MutableLiveData<>();
    public String tempText = "";
    public Note tempNote = null;
    public boolean sportFlag = false;
    public LiveData<List<Note>> data;
    // Reminder
    public String tempReminderTextValue = null;


    public LiveData<TabPositionState> tabStateChangeEvent = tabState;
    public LiveData<SingleLiveEvent<Note>> frameClickedSingleLiveEvent = frameClicked;
    public LiveData<Boolean> noteIsSelectedEvent = noteIsSelected;
    public LiveData<Boolean> noteIsDeletedEvent = noteIsDeleted;

    public ViewModelMain(Application application) {
        super(application);
        this.repository = new NoteRepository(application);
        this.data = repository.data;
        this.noteIsSelected.setValue(Boolean.FALSE);
        this.tabState.setValue(TabPositionState.ACTIVE);
    }

    @Override
    public List<Note> getTempSelectedNotes() {
        return tempSelectedNotes;
    }

    @Override
    public void unselectNote(Note note) {
        tempSelectedNotes.remove(note);
        if (tempSelectedNotes.isEmpty()) {
            noteIsSelected.setValue(Boolean.FALSE);
        }
    }

    public void setTabPosition(int position) {
        if (position == TabPositionState.ACTIVE.getTabIndex()) {
            tabState.setValue(TabPositionState.ACTIVE);
        } else if (position == TabPositionState.OLD.getTabIndex()) {
            tabState.setValue(TabPositionState.OLD);
        }
    }

    @Override
    public TabPositionState getTabState() {
        return this.tabStateChangeEvent.getValue();
    }

    @Override
    public void onFrameClick(Note note) {
        tempNote = note;
        frameClicked.setValue(new SingleLiveEvent<>(note));
    }

    @Override
    public boolean noteIsSelectedState() {
        return Boolean.TRUE.equals(noteIsSelectedEvent.getValue());
    }

    public void saveNoteAction() {
        if (tempText != null && !tempText.isEmpty()) {
            if (tempNote == null) {
                tempNote = new Note.Builder(tempText).isSportNote(sportFlag)
                        .noteState(TabPositionState.ACTIVE).build();
                repository.insert(tempNote);
            } else {
                int number = tempNote.getNumber();
                String textNote = tempText;
                boolean isSportNote = sportFlag;
                TabPositionState noteState = tempNote.getNoteState();
                tempNote = new Note.Builder(textNote)
                        .number(number)
                        .isSportNote(isSportNote)
                        .noteState(noteState)
                        .build();
                repository.update(tempNote);
            }
        }
        clearTempEntity();
    }

    @Override
    public void changeActualNoteState(Note note) {
        int number = note.getNumber();
        String textNote = note.getTextNote();
        boolean isSportNote = note.getIsSportNote();
        TabPositionState noteState = noteActualStateMutation(note);
        tempNote = new Note.Builder(textNote)
                .number(number)
                .isSportNote(isSportNote)
                .noteState(noteState)
                .build();
        repository.update(tempNote);
        clearTempEntity();
    }

    private TabPositionState noteActualStateMutation(Note note) {
        TabPositionState state = note.getNoteState();
        switch (state) {
            case ACTIVE:
                state = TabPositionState.OLD;
                break;
            case OLD:
                state = TabPositionState.ACTIVE;
                break;
        }
        return state;
    }


    public void deleteNotes() {
        for (Note tempSelectedNote : tempSelectedNotes) {
            repository.delete(tempSelectedNote);
        }
        noteIsDeleted.setValue(true);
    }

    @Override
    public void onFrameLongClick(Note note) {
        noteIsSelected.setValue(Boolean.TRUE);
        tempSelectedNotes.add(note);
    }

    public void clearDeleteNotesLiveEvent() {
        noteIsDeleted.setValue(Boolean.FALSE);
    }

    public void clearTempEntity() {
        tempNote = null;
        tempText = null;
        sportFlag = false;
        tempSelectedNotes.clear();
        noteIsSelected.setValue(Boolean.FALSE);
        //addReminder.setValue(Boolean.FALSE);
    }

    public void clearReminderBuffer() {
        tempReminderTextValue = null;
    }

}


