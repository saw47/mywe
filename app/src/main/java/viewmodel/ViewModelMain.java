package viewmodel;


import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.ArrayList;
import java.util.List;
import db.NoteEntity;
import db.NoteRepository;
import listener.NoteCardClickListener;
import object.Note;
import util.SingleLiveEvent;
import util.TabPositionState;

public class ViewModelMain extends AndroidViewModel implements NoteCardClickListener {

    public ViewModelMain(Application application) {
        super(application);
        this.repository = new NoteRepository(application);
        this.data = repository.data;
        this.isDataChanged = repository.entitiesData;
        this.noteIsSelected.setValue(Boolean.FALSE);
        this.tabState.setValue(TabPositionState.ACTIVE);
    }

    private final String TAG = "MW-VM";

    public String tempText= "";
    public Note tempNote = null;
    public boolean sportFlag = false;

    private final NoteRepository repository;
    public LiveData<List<NoteEntity>> isDataChanged;
    public LiveData<List<Note>> data;

    // Reminder
    public boolean addReminderFlag = false;
    private final MutableLiveData<Boolean> addReminder = new MutableLiveData<>();
    public LiveData<Boolean> addReminderEvent = addReminder;
    public void createCalendarEvent() {
        addReminder.setValue(true);
    }



    private final List<Note> tempSelectedNotes = new ArrayList<>();
    @Override
    public List<Note> getTempSelectedNotes()
    {
        return tempSelectedNotes;
    }
    @Override
    public void unselectNote(Note note)
    {
        tempSelectedNotes.remove(note);
        if (tempSelectedNotes.size() == 0) {
            noteIsSelected.setValue(Boolean.FALSE);
        }
    }

    private final MutableLiveData<TabPositionState> tabState = new MutableLiveData<>();
    public LiveData<TabPositionState> tabStateChangeEvent = tabState;
    public void setTabPosition(int position)
    {
        if (position == TabPositionState.ACTIVE.getTabIndex()) {
            tabState.setValue(TabPositionState.ACTIVE);
        } else if (position == TabPositionState.OLD.getTabIndex()) {
            tabState.setValue(TabPositionState.OLD);
        }
    }
    @Override
    public TabPositionState getTabState()
    {
        return this.tabStateChangeEvent.getValue();
    }

    private final MutableLiveData<SingleLiveEvent<Note>> frameClicked = new MutableLiveData<>();
    public LiveData<SingleLiveEvent<Note>> frameClickedSingleLiveEvent = frameClicked;
    @Override
    public void onFrameClick(Note note)
    {
        tempNote = note;
        frameClicked.setValue(new SingleLiveEvent<>(note));
    }

    private final MutableLiveData<Boolean> noteIsSelected = new MutableLiveData<>();
    public LiveData<Boolean> noteIsSelectedEvent = noteIsSelected;
    @Override
    public boolean noteIsSelectedState()
    {
        return Boolean.TRUE.equals(noteIsSelectedEvent.getValue());
    }

    private final MutableLiveData<Boolean> noteIsDeleted = new MutableLiveData<>();
    public LiveData<Boolean> noteIsDeletedEvent = noteIsDeleted;


    public void refreshDataList() {
        repository.convertData();
    }

    public void saveNoteOnClick() {
        if (tempText != null && !tempText.equals("")) {
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
        sportFlag  =false;
        tempSelectedNotes.clear();
        noteIsSelected.setValue(Boolean.FALSE);
        addReminderFlag = false;
        addReminder.setValue(false);
    }

}


