package dev.vinicius.todoapp.domain.subtodousecase

import dev.vinicius.todoapp.data.local.repository.impl.SubTodoItemRepository
import dev.vinicius.todoapp.util.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteSubTodoUseCase @Inject constructor(
    private val repository: SubTodoItemRepository
): UseCase.NoSource<Long>(){
    override suspend fun execute(param: Long): Flow<Unit> = flow {
        repository.getById(param)
            .collect{ subTodo ->

                repository.delete(subTodo)
                    .collect{
                        emit(it)
                    }

            }

    }

}