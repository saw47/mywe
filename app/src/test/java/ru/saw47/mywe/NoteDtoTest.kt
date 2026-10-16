package ru.saw47.mywe

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import db.NoteDto
import org.junit.Assert.assertEquals
import org.junit.Test
import util.TabPositionState

class NoteDtoTest {

    private val gson = Gson()
    private val listType = object : TypeToken<List<NoteDto>>() {}.type

    @Test
    fun serialize_producesCleanJsonNames() {
        val dto = NoteDto(1, "text", true, TabPositionState.ACTIVE)
        val json = gson.toJson(dto)
        assertEquals(
            """{"number":1,"textNote":"text","isSportNote":true,"noteState":"ACTIVE"}""",
            json
        )
    }

    @Test
    fun deserialize_parsesAllFields() {
        val json = """{"number":7,"textNote":"hello","isSportNote":false,"noteState":"OLD"}"""
        val dto = gson.fromJson(json, NoteDto::class.java)
        assertEquals(NoteDto(7, "hello", false, TabPositionState.OLD), dto)
    }

    @Test
    fun listRoundTrip_preservesData() {
        val list = listOf(
            NoteDto(1, "one", true, TabPositionState.ACTIVE),
            NoteDto(2, "two", false, TabPositionState.OLD),
            NoteDto(3, "три «кавычки» \"тест\" ✓", true, TabPositionState.UNDEF)
        )
        val json = gson.toJson(list, listType)
        val parsed: List<NoteDto> = gson.fromJson(json, listType)
        assertEquals(list, parsed)
    }

    @Test
    fun toNote_createsNoteWithAllFields() {
        val dto = NoteDto(5, "note text", true, TabPositionState.ACTIVE)
        val note = dto.toNote()
        assertEquals(5, note.getNumber())
        assertEquals("note text", note.getTextNote())
        assertEquals(true, note.getIsSportNote())
        assertEquals(TabPositionState.ACTIVE, note.getNoteState())
    }

    @Test
    fun fromNote_toNote_roundTrip() {
        val dto = NoteDto(9, "text", false, TabPositionState.OLD)
        assertEquals(dto, NoteDto.fromNote(dto.toNote()))
    }

    @Test
    fun deserialize_missingTextNote_usesEmptyText() {
        val json = """{"number":2,"isSportNote":true,"noteState":"ACTIVE"}"""
        val dto = gson.fromJson(json, NoteDto::class.java)
        assertEquals("", dto.toNote().getTextNote())
    }
}
