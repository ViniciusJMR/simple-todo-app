package dev.vinicius.todoapp.domain

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

data class TodoItem(
    val name: String,
    val creationDate: LocalDate,
    val description: String
){
    fun getFormatedDate(): String {
        val dtf = DateTimeFormatter.ofPattern("dd/MM")
        return creationDate.format(dtf)
    }
}