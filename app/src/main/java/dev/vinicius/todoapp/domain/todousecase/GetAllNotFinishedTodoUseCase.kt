package dev.vinicius.todoapp.domain.todousecase

import dev.vinicius.todoapp.data.local.repository.impl.TodoItemRepository
import dev.vinicius.todoapp.data.model.TodoWithSubTodos
import dev.vinicius.todoapp.domain.dto.SubTodoItemShow
import dev.vinicius.todoapp.domain.dto.TodoItemDTODetail
import dev.vinicius.todoapp.domain.dto.TodoItemDTOOutput
import dev.vinicius.todoapp.util.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllNotFinishedTodoUseCase @Inject constructor(
    private val todoItemRepository: TodoItemRepository
) : UseCase.NoParam<MutableList<TodoItemDTODetail>>() {

    override suspend fun execute(): Flow<MutableList<TodoItemDTODetail>> = flow {
        todoItemRepository.getAllWithSubTodos()
            .collect { list ->
                val newList =
                    list
                        .filter { it.todoItem.done == false }
                        .map { todoWithSubTodos ->
                            TodoItemDTODetail(
                                TodoItemDTOOutput(todoWithSubTodos.todoItem),
                                mapSubTodosToDTO(todoWithSubTodos)
                            )
                        }
                        .toMutableList()

                emit(newList)
            }
    }

    private fun mapSubTodosToDTO(todoWithSubTodos: TodoWithSubTodos) =
        todoWithSubTodos.subTodolist
            .map { subTodoItem ->
                SubTodoItemShow(subTodoItem)
            }
            .toMutableList()
}