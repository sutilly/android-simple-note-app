package com.example.andb_ex3_pa19l010

import java.util.*
import kotlin.collections.ArrayList

object DataManager {

    var notesList = ArrayList<Note>()

    init {
        createDefaultNotes()
    }

    private fun createDefaultNotes() {
        for (i in 1..20) {
            notesList.add(
                Note(
                    "Note #$i",
                    "This is note number $i",
                    formattingDates(Date()),
                    createID(),
                    "android.resource://com.example.andb_ex3_pa19l010/" + R.drawable.note)
                )
        }

    }

}