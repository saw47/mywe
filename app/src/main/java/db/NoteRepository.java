package db;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.room.RoomDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import object.Note;

public class NoteRepository {
    private final String TAG = "MW-REP";
    private final NoteDao dao;
    NoteRoomDatabase db;
    public final LiveData<List<NoteEntity>> entitiesData;
    public final MutableLiveData<List<Note>> data;

    public NoteRepository(Application application) {
        db = NoteRoomDatabase.getDatabase(application);
        dao = db.noteDao();
        entitiesData = dao.getAll();
        data = new MutableLiveData<>(new ArrayList<>());
        convertData();
    }

    public void convertData() {
        if (entitiesData.getValue() != null) {
            //data.setValue(new ArrayList<Note>());
            data.setValue(entitiesData.getValue()
                    .stream()
                    .map(Transform::toNote)
                    .collect(Collectors.toList()));
        }
    }


    public void insert(@NonNull Note note) {
        NoteRoomDatabase.databaseWriteExecutor.execute(() -> {
            dao.insert(Transform.toEntity(note));
            Log.d(TAG, "insert note " + note.getTextNote());
            Log.d(TAG, "insert size after " + ((dao.getAll().getValue() != null) ? dao.getAll().getValue().size() : -96));
            //refresh();
        });
    }

    public void update(@NonNull Note note) {
        NoteRoomDatabase.databaseWriteExecutor.execute(() -> {
            dao.update(Transform.toEntity(note));
            Log.d(TAG, "update note " + note.getTextNote());
            //refresh();
        });
    }

    public void delete(Note note) {
        NoteRoomDatabase.databaseWriteExecutor.execute(() -> {
            dao.delete(Transform.toEntity(note));
            Log.d(TAG, "delete note " + note.getTextNote());
        });
        Log.d(TAG, "Done delete note " + note.getTextNote());
    }

    public void tempClickRepo() {
        LiveData<List<NoteEntity>> l;
        l = dao.getAll();
        int allsize = (entitiesData.getValue() != null) ? entitiesData.getValue().size() : -12;
        int lsize = (l.getValue() != null) ? l.getValue().size() : -133;
        int datasize = (data.getValue() != null) ? data.getValue().size() : -1222222;
        Log.d(TAG, "!!!! tempClickRepo !!!! ->> size  allNotesLiveData - " + allsize + " ;;;" +
                " l  size -->>> " + lsize + "  datasize -> " + datasize );
    }

    /*
    public void refresh() {
        notes.clear();
        NoteRoomDatabase.databaseWriteExecutor.execute(() -> {
            try {
                Objects.requireNonNull(dao.getAll().getValue()).forEach(noteEntity -> notes.add(Transform.toNote(noteEntity)));
            } catch (NullPointerException npe) {
                Log.d(TAG, "Database is empty");
            }
                });
    }

     */
}
