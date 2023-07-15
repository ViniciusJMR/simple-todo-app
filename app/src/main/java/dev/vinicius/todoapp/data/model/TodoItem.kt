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
    val id: Long,
    val name: String,
    val creationDate: LocalDate,
    val description: String
)