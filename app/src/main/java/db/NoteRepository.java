package db;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import object.Note;

public class NoteRepository {
    public final LiveData<List<NoteEntity>> entitiesData;
    public final MutableLiveData<List<Note>> data;
    private final String TAG = "MW-REP";
    private final NoteDao dao;
    NoteRoomDatabase db;

    public NoteRepository(Application application) {
        db = NoteRoomDatabase.getDatabase(application);
        dao = db.noteDao();
        entitiesData = dao.getAll();
        data = new MutableLiveData<>(new ArrayList<>());
        convertData();
    }

    public void convertData() {
        if (entitiesData.getValue() != null) {
            data.setValue(entitiesData.getValue()
                    .stream()
                    .map(Transform::toNote)
                    .collect(Collectors.toList()));
        }
    }

    public void insert(@NonNull Note note) {
        NoteRoomDatabase.databaseWriteExecutor.execute(() -> {
            dao.insert(Transform.toEntity(note));
        });
    }

    public void update(@NonNull Note note) {
        NoteRoomDatabase.databaseWriteExecutor.execute(() -> {
            dao.update(Transform.toEntity(note));
        });
    }

    public void delete(Note note) {
        NoteRoomDatabase.databaseWriteExecutor.execute(() -> {
            dao.delete(Transform.toEntity(note));
        });
    }
}
