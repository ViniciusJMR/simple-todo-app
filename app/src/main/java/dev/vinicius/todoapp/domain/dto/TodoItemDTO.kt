package dev.vinicius.todoapp.domain.dto

import dev.vinicius.todoapp.data.model.SubTodoItem
import dev.vinicius.todoapp.data.model.TodoItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class TodoItemDTOOutput (
    val id: Long,
    val name: String,
    val creationDate: LocalDate,
    val endDate: LocalDate?,
    val description: String
){

    // Secondary constructor for mapping
    constructor(entity: TodoItem)
            : this(entity.id, entity.name, entity.creationDate, entity.endDate, entity.description)

    fun getFormattedCreationDate(): String {
        val dtf = DateTimeFormatter.ofPattern("dd/MM")
        return creationDate.format(dtf)
    }

    fun getFormattedEndDate() : String {
        val dtf = DateTimeFormatter.ofPattern("dd/MM")
        return endDate?.format(dtf) ?: ""
    }

    fun toEntity() = TodoItem(id, name, creationDate, endDate, description)
}

data class TodoItemDTOInput (
    var id: Long = 0,
    var name: String = "",
    var description: String = "",
    var endDate: String = "",
    var subTodoList: List<SubTodoItemShow> = listOf()
)

data class TodoItemDTODetail (
    var todoItemOutput:TodoItemDTOOutput? = null,
    var subTodoList: MutableList<SubTodoItemShow>? = null
){
}