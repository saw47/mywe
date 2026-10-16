package db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import util.TabPositionState

@Entity(tableName = "noteTable")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "number")
    @SerializedName("number")
    var number: Int = 0,
    @ColumnInfo(name = "textNote")
    @SerializedName("textNote")
    var textNote: String = "",
    @ColumnInfo(name = "isSportNote")
    @SerializedName("isSportNote")
    var isSportNote: Boolean = false,
    @ColumnInfo(name = "isActualNote")
    @SerializedName("noteState")
    var noteState: TabPositionState? = null
)