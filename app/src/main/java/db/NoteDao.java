package db;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM noteTable ORDER BY number DESC")
    LiveData<List<NoteEntity>> getAll();

    @Delete
    void delete(NoteEntity noteEntity);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(NoteEntity noteEntity);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    int update(NoteEntity noteEntity);

}
