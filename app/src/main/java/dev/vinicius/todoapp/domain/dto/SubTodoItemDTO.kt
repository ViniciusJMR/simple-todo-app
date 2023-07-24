package dev.vinicius.todoapp.domain.dto

import dev.vinicius.todoapp.data.model.SubTodoItem

data class SubTodoItemShow(
    val id: Long,
    val name: String,
    val done: Boolean
){
    constructor(entity: SubTodoItem): this(entity.id, entity.name, entity.done)
}

data class SubTodoItemInput(
    val name: String
)
