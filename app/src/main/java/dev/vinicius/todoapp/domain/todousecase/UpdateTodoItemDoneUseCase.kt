package dev.vinicius.todoapp.domain.todousecase

import android.util.Log
import dev.vinicius.todoapp.data.local.repository.impl.TodoItemRepository
import dev.vinicius.todoapp.data.model.TodoWithSubTodos
import dev.vinicius.todoapp.domain.dto.SubTodoItemShow
import dev.vinicius.todoapp.domain.dto.TodoItemDTOFinishTodo
import dev.vinicius.todoapp.exception.SubTodoException
import dev.vinicius.todoapp.util.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateTodoItemDoneUseCase @Inject constructor(
    private val todoItemRepository: TodoItemRepository
) : UseCase.NoSource<TodoItemDTOFinishTodo>() {

    companion object {
        val TAG = "UPDATETODOITEMDONEUSECASE"
    }

    override suspend fun execute(param: TodoItemDTOFinishTodo): Flow<Unit> =
        todoItemRepository.let {
            val todoItemOutput = param.todoDetail.todoItemOutput!!

            Log.d(TAG, "TodoItem: $todoItemOutput")
//            if (!param.forceSave) {
//                if (!isAllSubTodosFinished(param.todoDetail.subTodoList!!))
//                    throw SubTodoException("Task still has SubTasks")
//            }

            val todo = param.todoDetail.todoItemOutput!!.toEntity()
            it.update(todo)
        }
}


fun isAllSubTodosFinished(subTodos: List<SubTodoItemShow>): Boolean {
    val unfinishedSubTodos = subTodos.filter { !it.done }
    return unfinishedSubTodos.isEmpty()
}