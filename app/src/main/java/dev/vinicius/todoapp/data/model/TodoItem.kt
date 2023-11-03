package dev.vinicius.todoapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Entity
data class TodoItem(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var name: String,
    val creationDate: LocalDate,
    var endDate: LocalDate?,
    var description: String,
    var done: Boolean
) {
}