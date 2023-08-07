package dev.vinicius.todoapp.domain.todousecase

import dev.vinicius.todoapp.data.local.repository.impl.TodoItemRepository
import dev.vinicius.todoapp.data.model.SubTodoItem
import dev.vinicius.todoapp.domain.dto.SubTodoItemShow
import dev.vinicius.todoapp.domain.dto.TodoItemDTODetail
import dev.vinicius.todoapp.domain.dto.TodoItemDTOOutput
import dev.vinicius.todoapp.util.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllTodoDetailUseCase @Inject constructor(
    private val todoItemRepository: TodoItemRepository
) : UseCase.NoParam<MutableList<TodoItemDTODetail>>() {

    override suspend fun execute(): Flow<MutableList<TodoItemDTODetail>> = flow {
        todoItemRepository.getAllWithSubTodos()
            .collect { list ->
                val newList =
                    list.map { todoWithSubTodos ->
                        TodoItemDTODetail(
                            TodoItemDTOOutput(todoWithSubTodos.todoItem),
                            todoWithSubTodos.subTodolist
                                .map { subTodoItem ->
                                    SubTodoItemShow(subTodoItem)
                                }
                                .toMutableList()
                        )
                    }
                        .toMutableList()

                emit(newList)
            }
    }
}