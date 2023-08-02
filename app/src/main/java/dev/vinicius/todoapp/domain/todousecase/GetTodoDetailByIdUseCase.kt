package dev.vinicius.todoapp.domain.todousecase

import dev.vinicius.todoapp.data.local.repository.impl.TodoItemRepository
import dev.vinicius.todoapp.domain.dto.SubTodoItemShow
import dev.vinicius.todoapp.domain.dto.TodoItemDTODetail
import dev.vinicius.todoapp.domain.dto.TodoItemDTOOutput
import dev.vinicius.todoapp.util.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTodoDetailByIdUseCase @Inject constructor(
    private val todoItemRepository: TodoItemRepository
): UseCase<Long, TodoItemDTODetail> (){
    override suspend fun execute(param: Long): Flow<TodoItemDTODetail> = flow {
        val todoDetail = TodoItemDTODetail()
        todoItemRepository.getDetailTodoById(param)
            .collect {
                todoDetail.todoItemOutput = TodoItemDTOOutput(it.todoItem)
                todoDetail.subTodoList = it.subTodolist.map { subTodo ->
                    SubTodoItemShow(subTodo)
                }.toMutableList()
            }

        emit(todoDetail)
    }
}