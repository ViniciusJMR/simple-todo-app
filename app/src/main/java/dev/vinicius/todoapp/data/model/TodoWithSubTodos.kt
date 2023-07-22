package dev.vinicius.todoapp.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class TodoWithSubTodos(
    @Embedded val todoItem: TodoItem,
    @Relation(
        parentColumn = "id",
        entityColumn = "parentTodoId"
    )
    val subTodolist: List<SubTodoItem>
)
