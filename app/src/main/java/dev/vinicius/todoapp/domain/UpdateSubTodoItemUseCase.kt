package dev.vinicius.todoapp.domain

import dev.vinicius.todoapp.data.local.repository.impl.SubTodoItemRepository
import dev.vinicius.todoapp.domain.dto.SubTodoItemShow
import dev.vinicius.todoapp.util.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateSubTodoItemUseCase @Inject constructor(
    private val repository: SubTodoItemRepository
):UseCase<SubTodoItemShow, SubTodoItemShow>(){
    override suspend fun execute(param: SubTodoItemShow): Flow<SubTodoItemShow> = flow {

        repository.getByid(param.id)
            .collect{ subTodoItem ->
                repository.insertReturnInserted(subTodoItem)
                    .collect{
                        param.name = it.name
                        param.done = it.done
                    }

            }

        emit(param)
    }

}