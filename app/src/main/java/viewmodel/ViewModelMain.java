package viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BooleanSupplier;

import db.NoteRepository;
import listener.NoteCardClickListener;
import object.Note;
import util.SingleLiveEvent;

public class ViewModelMain extends AndroidViewModel implements NoteCardClickListener {

    private final String TAG = "MW-VM";

    public LiveData<List<Note>> data;
    private final NoteRepository repository;

    public String tempText= "";
    public Note tempNote = null;
    public List<Note> tempSelectedNotes = new ArrayList<>();

    private MutableLiveData<SingleLiveEvent<Note>> frameClicked = new MutableLiveData<>();
    public LiveData<SingleLiveEvent<Note>> frameClickedSingleLiveEvent = frameClicked;

    private MutableLiveData<Boolean> noteIsSelected = new MutableLiveData<>();
    public LiveData<Boolean> noteIsSelectedEvent = noteIsSelected;


    public ViewModelMain(Application application) {
        super(application);
        repository = new NoteRepository(application);
        this.data = repository.allNotesLiveData;
        noteIsSelected.setValue(false);
    }

    public void saveNoteOnClick() {
        Log.d(TAG, "saveNoteOnClick, tempNote -> " + tempNote + "; tempText -> " + tempText);
        if (tempText != null && !tempText.equals("")) {
            if (tempNote == null) {
                tempNote = new Note(tempText);
                repository.insert(tempNote);
            } else {
                int number = tempNote.getNumber();
                String textNote = tempText;
                Long deadLine = tempNote.getDeadLine();
                int delayReminder = tempNote.getDelayReminder();
                boolean important = tempNote.getImportant();
                tempNote = new Note(
                        number,
                        textNote,
                        deadLine,
                        delayReminder,
                        important
                );
                repository.update(tempNote);
            }
        }
        clearTempEntity();
    }

    public void deleteNotes() {
        for (Note tempSelectedNote : tempSelectedNotes) {
            repository.delete(tempSelectedNote);
        }
    }

    @Override
    public void onFrameClick(Note note)
    {
        if (Boolean.FALSE.equals(noteIsSelected.getValue()))
        {
            Log.d(TAG, "!event.getContentIfNotHandled()");
            tempNote = note;
            frameClicked.setValue(new SingleLiveEvent<>(note));
        }
    }

    @Override
    public void onFrameLongClick(Note note)
    {
        noteIsSelected.setValue(true);
        tempSelectedNotes.add(note);
    }



    public void clearTempEntity()
    {
        tempNote = null;
        tempText = null;
        tempSelectedNotes.clear();
        noteIsSelected.setValue(false);
    }
}


