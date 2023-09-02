package dev.vinicius.todoapp.exception

import dev.vinicius.todoapp.exception.field.FieldError

data class TodoException(
    override val message: String,
    val fields: HashMap<String, FieldError>? = null
): Exception(message) {

}