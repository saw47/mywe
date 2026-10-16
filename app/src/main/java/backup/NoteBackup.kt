package backup

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import db.NoteDao
import db.NoteDto
import db.Transform
import `object`.Note
import java.io.BufferedReader
import java.io.IOException
import java.io.Writer

class ImportException(message: String, cause: Throwable? = null) : Exception(message, cause)

object NoteBackup {

    private val gson = Gson()
    private val listType = object : TypeToken<List<NoteDto>>() {}.type

    @JvmStatic
    @Throws(IOException::class)
    fun exportNotes(dao: NoteDao, writer: Writer): Int {
        val entities = dao.getAllSync()
        val notes = entities.map { Transform.toNote(it) }
        val dtos = notes.map { NoteDto.fromNote(it) }
        gson.toJson(dtos, listType, writer)
        writer.flush()
        return dtos.size
    }

    @JvmStatic
    @Throws(IOException::class, ImportException::class)
    fun importNotes(dao: NoteDao, reader: BufferedReader): Int {
        val dtos: List<NoteDto>? = try {
            gson.fromJson(reader, listType)
        } catch (e: JsonSyntaxException) {
            throw ImportException("Invalid JSON file", e)
        } catch (e: IllegalStateException) {
            throw ImportException("Invalid JSON file content", e)
        }

        if (dtos == null) {
            throw ImportException("Empty JSON file")
        }

        for (dto in dtos) {
            if (dto.noteState == null) {
                throw ImportException("Invalid note state in JSON file")
            }
            val note = Note.Builder(dto.textNote ?: "")
                .isSportNote(dto.isSportNote)
                .noteState(dto.noteState)
                .build()
            dao.insert(Transform.toEntity(note))
        }
        return dtos.size
    }
}