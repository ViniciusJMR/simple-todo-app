package dev.vinicius.todoapp.domain

import dev.vinicius.todoapp.data.local.repository.impl.TodoItemRepository
import dev.vinicius.todoapp.domain.dto.TodoItemDTOInput
import dev.vinicius.todoapp.domain.dto.TodoItemDTOOutput
import dev.vinicius.todoapp.util.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteTodoUseCase @Inject constructor(
    private val repository: TodoItemRepository
): UseCase.NoSource<TodoItemDTOOutput>(){

    override suspend fun execute(param: TodoItemDTOOutput) =
        repository.delete(param.toEntity())

}