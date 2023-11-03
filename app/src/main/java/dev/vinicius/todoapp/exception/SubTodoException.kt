package dev.vinicius.todoapp.exception

data class SubTodoException(
    override val message: String
) : Exception(message){
}