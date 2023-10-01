package viewmodel;


import android.app.Application;
import android.util.Log;
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
    }

    private final String TAG = "MW-VM";

    public String tempText= "";
    public Note tempNote = null;
    public boolean sportFlag = false;
    private TabPositionState tabState = TabPositionState.UNDEF;

    private final NoteRepository repository;
    public LiveData<List<NoteEntity>> isDataChanged;
    public LiveData<List<Note>> data;


    private final List<Note> tempSelectedNotes = new ArrayList<>();
    @Override
    public List<Note> getTempSelectedNotes(){
        Log.d(TAG, "tempSelectedNotes SIZE - " + tempSelectedNotes.size());
        return tempSelectedNotes;
    }

    @Override
    public void unselectNote(Note note) {
        tempSelectedNotes.remove(note);
        if (tempSelectedNotes.size() == 0) {
            noteIsSelected.setValue(Boolean.FALSE);
        }
    }

    public void setFirstTimeTabPosition() {
        Log.d(TAG, "setFirstTimeTabPosition start - " + tabState);
        if (tabState == TabPositionState.UNDEF) {
            tabState = TabPositionState.ACTIVE;
        } else {
            throw new IllegalStateException("State " + tabState +
                    " does not allow transition on start.");
        }
        Log.d(TAG, "setFirstTimeTabPosition end - " + tabState);
    }

    public void setTabPosition(int position) {
        Log.d(TAG, "setTabPosition start - " + tabState);
        if (position == TabPositionState.ACTIVE.getTabIndex()) {
            tabState = TabPositionState.ACTIVE;
        } else if (position == TabPositionState.OLD.getTabIndex()) {
            tabState = TabPositionState.OLD;
        }
        Log.d(TAG, "setTabPosition end - " + tabState);
    }

    private final MutableLiveData<SingleLiveEvent<Note>> frameClicked = new MutableLiveData<>();
    public LiveData<SingleLiveEvent<Note>> frameClickedSingleLiveEvent = frameClicked;

    private final MutableLiveData<Boolean> noteIsSelected = new MutableLiveData<>();
    public LiveData<Boolean> noteIsSelectedEvent = noteIsSelected;
    @Override
    public boolean noteIsSelectedState() {
        Log.d(TAG, "noteIsSelectedState 2 - " + Boolean.TRUE.equals(noteIsSelectedEvent.getValue()));
        return Boolean.TRUE.equals(noteIsSelectedEvent.getValue());
    }

    private final MutableLiveData<Boolean> noteIsDeleted = new MutableLiveData<>();
    public LiveData<Boolean> noteIsDeletedEvent = noteIsDeleted;


    public void refreshDataList() {
        Log.d(TAG, "refreshDataList -> ");
        repository.convertData();
    }

    public void saveNoteOnClick() {
        Log.d(TAG, "saveNoteOnClick, tempNote -> " + tempNote + "; tempText -> " +
                tempText + "sportflag -> " + sportFlag);
        if (tempText != null && !tempText.equals("")) {
            if (tempNote == null) {
                tempNote = new Note.Builder(tempText).isSportNote(sportFlag)
                                   .noteState(TabPositionState.ACTIVE).build();
                Log.d(TAG, "saveNoteOnClick, NOTE NEW text ->" + tempText + "sportflag -> "
                        + sportFlag + "ACTUAL -> " + tempNote.getNoteState());
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
        Log.d(TAG, "saveNoteOnClick end repo size -> " + ((repository.data.getValue() != null) ? repository.data.getValue().size() : -45));
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
        Log.d(TAG, "noteActualStateMutation start - " + tabState);
        switch (state) {
            case ACTIVE:
                state = TabPositionState.OLD;
                break;
            case OLD:
                state = TabPositionState.ACTIVE;
                break;
            case UNDEF:
                throw new IllegalStateException("State " + TabPositionState.UNDEF + " not allowed.");
        }
        return state;
    }


    @Override
    public TabPositionState getTabState() {
        return this.tabState;
    }

    public void deleteNotes() {
        for (Note tempSelectedNote : tempSelectedNotes) {
            Log.d(TAG, "tempSelectedNote delete in tempSelectedNotes " + tempSelectedNote.getTextNote());
            repository.delete(tempSelectedNote);
        }
        Log.d(TAG, "refresh repo from deleteNotes (DONE)");
        //repository.refresh();
        noteIsDeleted.setValue(true);
    }

    @Override
    public void onFrameClick(Note note) {
        tempNote = note;
        frameClicked.setValue(new SingleLiveEvent<>(note));
    }

    @Override
    public void onFrameLongClick(Note note) {
        noteIsSelected.setValue(Boolean.TRUE);
        tempSelectedNotes.add(note);
        Log.d(TAG, "note add to tempSelectedNotes " + note.getTextNote());
        Log.d(TAG, "tempSelectedNotes size " + tempSelectedNotes.size());
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
    }

    public void tempClick() {
        //TODO debug
        repository.tempClickRepo();
    }
}


