package db;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

@Dao
public interface EntityDao {
    @Insert
    void insertAll();

    @Delete
    void delete();
}
