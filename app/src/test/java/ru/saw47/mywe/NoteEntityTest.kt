package ru.saw47.mywe

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import db.NoteEntity
import org.junit.Assert.assertEquals
import org.junit.Test
import util.TabPositionState

class NoteEntityTest {

    private val gson = Gson()
    private val listType = object : TypeToken<List<NoteEntity>>() {}.type

    @Test
    fun serialize_producesCleanJsonNames() {
        val entity = NoteEntity(1, "text", true, TabPositionState.ACTIVE)
        val json = gson.toJson(entity)
        assertEquals(
            """{"number":1,"textNote":"text","isSportNote":true,"noteState":"ACTIVE"}""",
            json
        )
    }

    @Test
    fun deserialize_parsesAllFields() {
        val json = """{"number":7,"textNote":"hello","isSportNote":false,"noteState":"OLD"}"""
        val entity = gson.fromJson(json, NoteEntity::class.java)
        assertEquals(NoteEntity(7, "hello", false, TabPositionState.OLD), entity)
    }

    @Test
    fun listRoundTrip_preservesData() {
        val list = listOf(
            NoteEntity(1, "one", true, TabPositionState.ACTIVE),
            NoteEntity(2, "two", false, TabPositionState.OLD),
            NoteEntity(3, "три «кавычки» \"тест\" ✓", true, TabPositionState.UNDEF)
        )
        val json = gson.toJson(list, listType)
        val parsed: List<NoteEntity> = gson.fromJson(json, listType)
        assertEquals(list, parsed)
    }

    @Test
    fun deserialize_missingTextNote_usesEmptyText() {
        val json = """{"number":2,"isSportNote":true,"noteState":"ACTIVE"}"""
        val entity = gson.fromJson(json, NoteEntity::class.java)
        assertEquals("", entity.textNote)
    }
}