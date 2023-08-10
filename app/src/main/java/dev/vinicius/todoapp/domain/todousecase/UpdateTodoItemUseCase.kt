package dev.vinicius.todoapp.domain.todousecase

import dev.vinicius.todoapp.data.local.repository.impl.TodoItemRepository
import dev.vinicius.todoapp.data.model.TodoItem
import dev.vinicius.todoapp.domain.dto.TodoItemDTOInput
import dev.vinicius.todoapp.util.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import javax.inject.Inject

class UpdateTodoItemUseCase @Inject constructor(
    private val repository: TodoItemRepository
): UseCase.NoSource<TodoItemDTOInput>(){
    override suspend fun execute(param: TodoItemDTOInput): Flow<Unit> = flow {
        repository.getById(param.id)
            .collect{ todo ->
                val endDate = LocalDate.parse(param.endDate)
                val newTodo =
                    TodoItem(
                        id = todo.id,
                        name = param.name,
                        creationDate = todo.creationDate,
                        endDate = endDate,
                        description = param.description
                    )

                repository.insert(newTodo)
                    .collect{
                        emit(Unit)
                    }
            }
    }
}