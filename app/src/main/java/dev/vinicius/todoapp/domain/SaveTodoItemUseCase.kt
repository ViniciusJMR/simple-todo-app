package dev.vinicius.todoapp.domain

import dev.vinicius.todoapp.data.local.repository.impl.TodoItemRepository
import dev.vinicius.todoapp.data.model.TodoItem
import dev.vinicius.todoapp.domain.dto.TodoItemDTOInput
import dev.vinicius.todoapp.util.UseCase
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class SaveTodoItemUseCase @Inject constructor(
    private val todoItemRepo: TodoItemRepository
) : UseCase.NoSource<TodoItemDTOInput>() {
    override suspend fun execute(param: TodoItemDTOInput): Flow<Unit> {
        val newTodoItem = TodoItem(
            name = param.name,
            creationDate = LocalDate.now(),
            description = param.description
        )
        return todoItemRepo.insert(newTodoItem)
    }
}