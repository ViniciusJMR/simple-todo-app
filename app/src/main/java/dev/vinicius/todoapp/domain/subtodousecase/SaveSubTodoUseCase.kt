package dev.vinicius.todoapp.domain.subtodousecase

import dev.vinicius.todoapp.data.local.repository.impl.SubTodoItemRepository
import dev.vinicius.todoapp.data.model.SubTodoItem
import dev.vinicius.todoapp.domain.dto.SubTodoItemShow
import dev.vinicius.todoapp.util.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SaveSubTodoUseCase @Inject constructor(
    private val repository: SubTodoItemRepository
): UseCase<Pair<Long, SubTodoItemShow>, SubTodoItemShow>() {
    override suspend fun execute(param: Pair<Long, SubTodoItemShow>): Flow<SubTodoItemShow> = flow {
        val newSubTodoItem = SubTodoItem(
            parentTodoId = param.first,
            name = param.second.name,
            done = param.second.done
        )

        repository.insertReturnInserted(newSubTodoItem)
            .collect{
                val newSubTodoShow = SubTodoItemShow(
                    id = it.id,
                    name = it.name,
                    done = it.done
                )

                emit(newSubTodoShow)
            }
    }
}