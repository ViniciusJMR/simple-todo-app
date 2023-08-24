package dev.vinicius.todoapp.domain.todousecase

import dev.vinicius.todoapp.data.local.repository.impl.SubTodoItemRepository
import dev.vinicius.todoapp.data.local.repository.impl.TodoItemRepository
import dev.vinicius.todoapp.data.model.SubTodoItem
import dev.vinicius.todoapp.data.model.TodoItem
import dev.vinicius.todoapp.domain.dto.TodoItemDTOInput
import dev.vinicius.todoapp.util.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import java.time.format.DateTimeParseException
import javax.inject.Inject

class SaveTodoItemUseCase @Inject constructor(
    private val todoItemRepo: TodoItemRepository,
    private val subTodoItemRepo: SubTodoItemRepository
) : UseCase.NoSource<TodoItemDTOInput>() {
    override suspend fun execute(param: TodoItemDTOInput): Flow<Unit> = flow {
        val date: LocalDate? = try {
            LocalDate.parse(param.endDate)
        } catch (e: DateTimeParseException) {
            null
        }

        val newTodoItem = TodoItem(
            name = param.name,
            creationDate = LocalDate.now(),
            endDate = date,
            description = param.description
        )
        todoItemRepo.insert(newTodoItem).collect { id ->
            param.subTodoList
                .map {
                    SubTodoItem(parentTodoId = id, name = it.name, done = it.done)
                }
                .also { list ->
                    subTodoItemRepo.insertAll(list).collect { emit(Unit) }
                }
        }
    }
}