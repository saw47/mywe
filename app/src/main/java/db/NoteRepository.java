package db;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import object.Note;

public class NoteRepository {
    private final String TAG = "MW-REP";

    private final NoteDao dao;
    private List<Note> notes = new ArrayList<>();
    public LiveData<List<Note>> allNotesLiveData = new MutableLiveData<>(notes);

    public NoteRepository(Application application) {
        NoteRoomDatabase db = NoteRoomDatabase.getDatabase(application);
        dao = db.noteDao();
        refresh();
    }


    public void insert(@NonNull Note note) {
        NoteRoomDatabase.databaseWriteExecutor.execute(() -> {
            dao.insert(Transform.toEntity(note));
            Log.d(TAG, "insert note -> " + note.getNumber());
            refresh();
        });
    }

    public void update(@NonNull Note note) {
        NoteRoomDatabase.databaseWriteExecutor.execute(() -> {
            int result = dao.update(Transform.toEntity(note));
            Log.d(TAG, "update note -> " + note.getNumber() + " result -> " + result);
            refresh();
        });
    }

    public void delete(Note note) {
        NoteRoomDatabase.databaseWriteExecutor.execute(() -> {
            int result = dao.delete(Transform.toEntity(note));
            Log.d(TAG, "delete note -> " + note.getNumber() + " result -> " + result);
            refresh();
        });
    }

    public void refresh() {
        notes.clear();
        NoteRoomDatabase.databaseWriteExecutor.execute(() -> {
                    dao.getAll().forEach(noteEntity -> notes.add(Transform.toNote(noteEntity)));
                });
    }
}
