package dev.vinicius.todoapp.domain.dto

import dev.vinicius.todoapp.data.model.TodoItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class TodoItemDTOOutput (
    val id: Long,
    val name: String,
    val creationDate: LocalDate,
    val description: String
){

    // Secondary constructor for mapping
    constructor(entity: TodoItem)
            : this(entity.id, entity.name, entity.creationDate, entity.description)

    fun getFormattedDate(): String {
        val dtf = DateTimeFormatter.ofPattern("dd/MM")
        return creationDate.format(dtf)
    }
}

data class TodoItemDTOInput (
    var name: String,
    var description: String
)