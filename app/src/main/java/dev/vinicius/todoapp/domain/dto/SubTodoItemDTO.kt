package dev.vinicius.todoapp.domain.dto

import dev.vinicius.todoapp.data.model.SubTodoItem

data class SubTodoItemShow(
    var id: Long = 0,
    var name: String,
    var done: Boolean
){
    constructor(entity: SubTodoItem): this(entity.id, entity.name, entity.done)
}

data class SubTodoItemInput(
    val name: String
)
