package ru.saw47.mywe

import androidx.lifecycle.LiveData
import backup.ImportException
import backup.NoteBackup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import db.NoteDao
import db.NoteEntity
import db.Transform
import `object`.Note
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import util.TabPositionState
import java.io.BufferedReader
import java.io.StringReader
import java.io.StringWriter

class NoteBackupTest {

    private val gson = Gson()
    private val listType = object : TypeToken<List<NoteEntity>>() {}.type

    private class FakeDao : NoteDao {
        val inserted = mutableListOf<NoteEntity>()
        val stored = mutableListOf<NoteEntity>()

        override fun getAll(): LiveData<List<NoteEntity>>? = null

        override fun getAllSync(): MutableList<NoteEntity> = stored

        override fun delete(noteEntity: NoteEntity) {
            stored.remove(noteEntity)
        }

        override fun insert(noteEntity: NoteEntity) {
            inserted.add(noteEntity)
        }

        override fun update(noteEntity: NoteEntity): Int = 0
    }

    @Test
    fun exportNotes_writesJsonArrayOfEntities() {
        val note1 = Note.Builder("one").number(1).isSportNote(true)
            .noteState(TabPositionState.ACTIVE).build()
        val note2 = Note.Builder("two").number(2).isSportNote(false)
            .noteState(TabPositionState.OLD).build()
        val dao = FakeDao()
        dao.stored.add(Transform.toEntity(note1))
        dao.stored.add(Transform.toEntity(note2))

        val writer = StringWriter()
        val count = NoteBackup.exportNotes(dao, writer)

        assertEquals(2, count)
        val parsed: List<NoteEntity> = gson.fromJson(writer.toString(), listType)
        assertEquals(Transform.toEntity(note1), parsed[0])
        assertEquals(Transform.toEntity(note2), parsed[1])
    }

    @Test
    fun exportNotes_emptyDb_writesEmptyArray() {
        val writer = StringWriter()
        val count = NoteBackup.exportNotes(FakeDao(), writer)
        assertEquals(0, count)
        assertEquals("[]", writer.toString())
    }

    @Test
    fun importNotes_insertsEntitiesWithRegeneratedIds() {
        val json = """
            [{"number":10,"textNote":"a","isSportNote":false,"noteState":"ACTIVE"},
             {"number":11,"textNote":"b","isSportNote":true,"noteState":"OLD"}]
        """.trimIndent()
        val dao = FakeDao()

        val count = NoteBackup.importNotes(dao, BufferedReader(StringReader(json)))

        assertEquals(2, count)
        assertEquals(2, dao.inserted.size)
        val notes = dao.inserted.map { Transform.toNote(it) }
        assertEquals(0, notes[0].getNumber())
        assertEquals(0, notes[1].getNumber())
        assertEquals("a", notes[0].getTextNote())
        assertEquals(true, notes[1].getIsSportNote())
        assertEquals(TabPositionState.OLD, notes[1].getNoteState())
    }

    @Test
    fun importNotes_emptyArray_insertsNothing() {
        val dao = FakeDao()
        val count = NoteBackup.importNotes(dao, BufferedReader(StringReader("[]")))
        assertEquals(0, count)
        assertEquals(0, dao.inserted.size)
    }

    @Test
    fun importNotes_malformedJson_throwsImportException() {
        val dao = FakeDao()
        try {
            NoteBackup.importNotes(dao, BufferedReader(StringReader("{not json")))
            fail("expected ImportException")
        } catch (e: ImportException) {
            // expected
        }
    }

    @Test
    fun importNotes_unknownEnum_throwsImportException() {
        val json = """[{"number":1,"textNote":"x","isSportNote":false,"noteState":"BOGUS"}]"""
        val dao = FakeDao()
        try {
            NoteBackup.importNotes(dao, BufferedReader(StringReader(json)))
            fail("expected ImportException")
        } catch (e: ImportException) {
            // expected
        }
    }

    @Test
    fun importNotes_emptyFile_throwsImportException() {
        val dao = FakeDao()
        try {
            NoteBackup.importNotes(dao, BufferedReader(StringReader("")))
            fail("expected ImportException")
        } catch (e: ImportException) {
            // expected
        }
    }
}
