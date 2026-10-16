package db

import com.google.gson.annotations.SerializedName
import `object`.Note
import util.TabPositionState

data class NoteDto(
    @SerializedName("number") val number: Int,
    @SerializedName("textNote") val textNote: String?,
    @SerializedName("isSportNote") val isSportNote: Boolean,
    @SerializedName("noteState") val noteState: TabPositionState?
) {

    fun toNote(): Note = Note.Builder(textNote ?: "")
        .number(number)
        .isSportNote(isSportNote)
        .noteState(noteState)
        .build()

    companion object {

        fun fromNote(note: Note): NoteDto = NoteDto(
            number = note.getNumber(),
            textNote = note.getTextNote(),
            isSportNote = note.getIsSportNote(),
            noteState = note.getNoteState()
        )
    }
}