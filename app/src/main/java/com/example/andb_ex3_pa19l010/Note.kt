package com.example.andb_ex3_pa19l010

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

data class Note(
    var title: String?,
    var content: String?,
    var date: String?,
    var id: Int,
    var imgSrc: String?
    ) : Serializable {

    override fun toString(): String {
        return ("$title:\n$content\n($date)")
    }

}
    fun formattingDates(date: Date): String {
        val pattern = "dd/MM/yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern)
        return simpleDateFormat.format(date)
    }

    fun createID(): Int {
        val random = Random()
        return random.nextInt(600)
    }


