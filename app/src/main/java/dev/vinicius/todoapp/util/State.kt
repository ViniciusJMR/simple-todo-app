package dev.vinicius.todoapp.util

sealed class State<out T: Any>{
    object Loading: State<Nothing>()
    data class Success<out T: Any>(val response:T?): State<T>()
    data class Error(val error:Throwable): State<Nothing>()
}