package dev.vinicius.todoapp.domain

import android.util.Log
import dev.vinicius.todoapp.data.local.repository.impl.SubTodoItemRepository
import dev.vinicius.todoapp.data.local.repository.impl.TodoItemRepository
import dev.vinicius.todoapp.data.model.SubTodoItem
import dev.vinicius.todoapp.data.model.TodoItem
import dev.vinicius.todoapp.domain.dto.TodoItemDTOInput
import dev.vinicius.todoapp.util.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import javax.inject.Inject

class SaveTodoItemUseCase @Inject constructor(
    private val todoItemRepo: TodoItemRepository,
    private val subTodoItemRepo: SubTodoItemRepository
) : UseCase.NoSource<TodoItemDTOInput>() {
    override suspend fun execute(param: TodoItemDTOInput): Flow<Unit> = flow {
        val date: LocalDate? = if (param.endDate.isBlank() or param.endDate.isEmpty()){
            null
        }
        else{
            LocalDate.parse(param.endDate)
        }

        val newTodoItem = TodoItem(
            name = param.name,
            creationDate = LocalDate.now(),
            endDate = date,
            description = param.description
        )
        var inserted = 0L
        todoItemRepo.insert(newTodoItem).collect{
            inserted = it
        }

        val list = mutableListOf<SubTodoItem>()

        if(param.subTodoList.isNotEmpty()){
            param.subTodoList.forEach {
                val subTodo = SubTodoItem(parentTodoId = inserted, name = it.name, done = it.done)
                list.add(subTodo)
            }
        }
        subTodoItemRepo.insertAll(list).collect{ emit(Unit)}
    }
}