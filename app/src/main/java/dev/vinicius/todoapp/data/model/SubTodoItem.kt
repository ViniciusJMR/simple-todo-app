package dev.vinicius.todoapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SubTodoItem(
    @PrimaryKey
    val id: Long,
    val parentTodoId: Long,
    val name: String,
    val done: Boolean,
)
