package dev.vinicius.todoapp.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = TodoItem::class,
        parentColumns = ["id"],
        childColumns = ["parentTodoId"],
        onDelete = CASCADE
    )]
)
data class SubTodoItem(
    @PrimaryKey
    val id: Long,
    val parentTodoId: Long,
    val name: String,
    val done: Boolean,
)
