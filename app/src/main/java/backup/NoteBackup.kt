package backup

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import db.NoteDao
import db.NoteEntity
import java.io.BufferedReader
import java.io.IOException
import java.io.Writer

class ImportException(message: String, cause: Throwable? = null) : Exception(message, cause)

object NoteBackup {

    private val gson = Gson()
    private val listType = object : TypeToken<List<NoteEntity>>() {}.type

    @JvmStatic
    @Throws(IOException::class)
    fun exportNotes(dao: NoteDao, writer: Writer): Int {
        val entities = dao.getAllSync()
        gson.toJson(entities, listType, writer)
        writer.flush()
        return entities.size
    }

    @JvmStatic
    @Throws(IOException::class, ImportException::class)
    fun importNotes(dao: NoteDao, reader: BufferedReader): Int {
        val entities: List<NoteEntity>? = try {
            gson.fromJson(reader, listType)
        } catch (e: JsonSyntaxException) {
            throw ImportException("Invalid JSON file", e)
        } catch (e: IllegalStateException) {
            throw ImportException("Invalid JSON file content", e)
        }

        if (entities == null) {
            throw ImportException("Empty JSON file")
        }

        for (entity in entities) {
            if (entity.noteState == null) {
                throw ImportException("Invalid note state in JSON file")
            }
            dao.insert(
                NoteEntity(
                    textNote = entity.textNote,
                    isSportNote = entity.isSportNote,
                    noteState = entity.noteState
                )
            )
        }
        return entities.size
    }
}