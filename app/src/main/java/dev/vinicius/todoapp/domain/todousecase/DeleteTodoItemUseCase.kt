package dev.vinicius.todoapp.domain.todousecase

import dev.vinicius.todoapp.data.local.repository.impl.TodoItemRepository
import dev.vinicius.todoapp.util.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteTodoItemUseCase @Inject constructor(
    private val repository: TodoItemRepository
): UseCase.NoSource<Long>() {
    override suspend fun execute(param: Long): Flow<Unit> = flow {
        repository.getById(param)
            .collect{
                repository.delete(it)
                    .collect{
                        emit(Unit)
                    }
            }

    }
}