package dev.vinicius.todoapp.domain

import java.text.SimpleDateFormat
import java.time.LocalDate

data class TodoItem(
    val name: String,
    val creationDate: LocalDate,
    val description: String
){
    fun formatDate(): String {
        val sdf = SimpleDateFormat.getDateInstance()
        return sdf.format(creationDate)
    }
}