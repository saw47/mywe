package ru.saw47.mywe

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import backup.ImportException
import backup.NoteBackup
import db.NoteRoomDatabase
import db.Transform
import `object`.Note
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import util.TabPositionState
import java.io.BufferedReader
import java.io.StringReader
import java.io.StringWriter

@RunWith(AndroidJUnit4::class)
class NoteBackupInstrumentedTest {

    private lateinit var db: NoteRoomDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, NoteRoomDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun exportThenImport_roundTripsNotes() {
        val note1 = Note.Builder("первая заметка").number(1).isSportNote(true)
            .noteState(TabPositionState.ACTIVE).build()
        val note2 = Note.Builder("second \"quoted\" ✓").number(2).isSportNote(false)
            .noteState(TabPositionState.OLD).build()
        db.noteDao().insert(Transform.toEntity(note1))
        db.noteDao().insert(Transform.toEntity(note2))

        val writer = StringWriter()
        val exportCount = NoteBackup.exportNotes(db.noteDao(), writer)
        assertEquals(2, exportCount)

        db.noteDao().getAllSync().forEach { db.noteDao().delete(it) }
        assertEquals(0, db.noteDao().getAllSync().size)

        val importCount = NoteBackup.importNotes(
            db.noteDao(),
            BufferedReader(StringReader(writer.toString()))
        )
        assertEquals(2, importCount)

        val imported = db.noteDao().getAllSync().map { Transform.toNote(it) }
        assertEquals(2, imported.size)
        assertEquals("первая заметка", imported.first().getTextNote())
        assertTrue(imported.any { it.getTextNote() == "second \"quoted\" ✓" })
        assertTrue(imported.any { it.getIsSportNote() })
        assertTrue(imported.any { it.getNoteState() == TabPositionState.OLD })
    }

    @Test
    fun import_malformedJson_throwsImportException() {
        try {
            NoteBackup.importNotes(
                db.noteDao(),
                BufferedReader(StringReader("{bad"))
            )
            fail("expected ImportException")
        } catch (e: ImportException) {
            // expected
        }
    }
}
