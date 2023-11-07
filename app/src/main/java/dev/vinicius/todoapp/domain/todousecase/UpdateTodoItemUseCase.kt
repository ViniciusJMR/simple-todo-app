package dev.vinicius.todoapp.domain.todousecase

import dev.vinicius.todoapp.data.local.repository.impl.TodoItemRepository
import dev.vinicius.todoapp.data.model.TodoItem
import dev.vinicius.todoapp.domain.dto.TodoItemDTOInput
import dev.vinicius.todoapp.exception.TodoException
import dev.vinicius.todoapp.exception.field.FieldError
import dev.vinicius.todoapp.util.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import javax.inject.Inject

class UpdateTodoItemUseCase @Inject constructor(
    private val repository: TodoItemRepository
): UseCase.NoSource<TodoItemDTOInput>(){
    override suspend fun execute(param: TodoItemDTOInput): Flow<Unit> = flow {
        val fields: HashMap<String, FieldError> = hashMapOf()

        param.name.ifBlank {
            fields["name"] = FieldError.BLANKORNULL
        }

        var endDate: LocalDate? = null
        if (param.endDate.isNotBlank()) {
            endDate = try {
                val dtf = DateTimeFormatter.ofPattern("dd/MM/yy")
                LocalDate.parse(param.endDate, dtf)
            } catch (e: DateTimeParseException) {
                fields["date"] = FieldError.DATEFORMAT
                null
            }
        }

        if (fields.keys.size > 0){
            throw TodoException("Creation Error", fields)
        }

        repository.getById(param.id)
            .collect{ todo ->
                val newTodo =
                    TodoItem(
                        id = todo.id,
                        name = param.name,
                        creationDate = todo.creationDate,
                        endDate = endDate,
                        description = param.description,
                        done = todo.done
                    )

                repository.update(newTodo)
                    .collect{
                        emit(Unit)
                    }
            }
    }
}