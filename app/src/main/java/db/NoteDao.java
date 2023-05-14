package db;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import object.Note;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM noteTable ORDER BY number ASC")
    LiveData<List<Note>> getAll();


    @Delete
    void delete();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Note note);

}
