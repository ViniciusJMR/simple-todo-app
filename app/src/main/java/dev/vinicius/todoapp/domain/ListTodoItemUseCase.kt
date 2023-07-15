package dev.vinicius.todoapp.domain

import dev.vinicius.todoapp.data.local.repository.impl.TodoItemRepository
import dev.vinicius.todoapp.data.model.TodoItem
import dev.vinicius.todoapp.util.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ListTodoItemUseCase @Inject constructor(
    private val todoItemRepo: TodoItemRepository
): UseCase.NoParam<List<TodoItem>>() {

    override suspend fun execute(): Flow<List<TodoItem>> =
        todoItemRepo.getAll()
}