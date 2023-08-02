package dev.vinicius.todoapp.domain.subtodousecase

import android.util.Log
import dev.vinicius.todoapp.data.local.repository.impl.SubTodoItemRepository
import dev.vinicius.todoapp.data.model.SubTodoItem
import dev.vinicius.todoapp.domain.dto.SubTodoItemShow
import dev.vinicius.todoapp.util.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateSubTodoItemUseCase @Inject constructor(
    private val repository: SubTodoItemRepository
):UseCase<SubTodoItemShow, SubTodoItemShow>(){
    override suspend fun execute(param: SubTodoItemShow): Flow<SubTodoItemShow> = flow {
        repository.getById(param.id)
            .collect{ subTodoItem ->

                val newSubTodoItem = SubTodoItem(
                    id = subTodoItem.id,
                    parentTodoId = subTodoItem.parentTodoId,
                    name = param.name,
                    done = param.done
                )
                repository.insertReturnInserted(newSubTodoItem)
                    .collect{
                        val subTodo = SubTodoItemShow(id = it.id, name = it.name,done=it.done)
                        emit(subTodo)
                    }
            }

    }

}