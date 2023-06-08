package db;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import object.Note;

public class NoteRepository {
    private NoteDao mNoteDao;
    private LiveData<List<NoteEntity>> mAllNotes;

    public NoteRepository(Application application) {
        NoteRoomDatabase db = NoteRoomDatabase.getDatabase(application);
        mNoteDao = db.noteDao();
        mAllNotes = mNoteDao.getAll();
    }

    void insert(Note note) {
        NoteRoomDatabase.databaseWriteExecutor.execute(() -> {
            mNoteDao.insert(Transform.toEntity(note));
        });
    }

    void delete(Note note) {
        NoteRoomDatabase.databaseWriteExecutor.execute(() -> {
            mNoteDao.delete(Transform.toEntity(note));
        });
    }
}
