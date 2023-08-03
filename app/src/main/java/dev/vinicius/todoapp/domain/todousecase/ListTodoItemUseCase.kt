package dev.vinicius.todoapp.domain.todousecase

import dev.vinicius.todoapp.data.local.repository.impl.TodoItemRepository
import dev.vinicius.todoapp.domain.dto.TodoItemDTOOutput
import dev.vinicius.todoapp.util.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ListTodoItemUseCase @Inject constructor(
    private val todoItemRepo: TodoItemRepository
): UseCase.NoParam<MutableList<TodoItemDTOOutput>>() {

    override suspend fun execute(): Flow<MutableList<TodoItemDTOOutput>> =
        todoItemRepo.getAll().map {
            it.map { todoItem ->
                TodoItemDTOOutput(todoItem)
            }.toMutableList()
        }
}