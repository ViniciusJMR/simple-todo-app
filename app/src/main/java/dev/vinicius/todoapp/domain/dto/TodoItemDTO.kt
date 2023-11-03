package dev.vinicius.todoapp.domain.dto

import dev.vinicius.todoapp.App
import dev.vinicius.todoapp.R
import dev.vinicius.todoapp.data.model.SubTodoItem
import dev.vinicius.todoapp.data.model.TodoItem
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import kotlin.math.absoluteValue

data class TodoItemDTOOutput(
    val id: Long,
    val name: String,
    val creationDate: LocalDate,
    val endDate: LocalDate?,
    val description: String,
    var done: Boolean,
) {

    private val dtf = DateTimeFormatter.ofPattern("dd/MM/yy")

    // Secondary constructor for mapping
    constructor(entity: TodoItem)
            : this(
        entity.id,
        entity.name,
        entity.creationDate,
        entity.endDate,
        entity.description,
        entity.done
    )

    fun getFormattedCreationDate(): String {
        return creationDate.format(dtf)
    }

    fun getFormattedEndDate(): String? {
        return endDate?.format(dtf)
    }

    fun getDaysLeft() =
        endDate?.let {
            Period
                .between(LocalDate.now(), endDate)
                .days
        }

    fun getDaysLeftResource(): String {
        val context = App.getContext()
        val period = endDate?.let {
            Period
                .between(LocalDate.now(), endDate)
                .days
        }

        var stringResource = ""
        period?.let { days ->
            stringResource = if (days == 0)
                context.getString(R.string.txt_today)
            else if (days > 0)
                context.resources.getQuantityString(R.plurals.number_of_days_positive, days, days)
            else
                context.resources.getQuantityString(
                    R.plurals.number_of_days_negative,
                    days.absoluteValue,
                    days.absoluteValue
                )
        }
        return stringResource
    }

    fun toEntity() = TodoItem(id, name, creationDate, endDate, description, done)
}

data class TodoItemDTOInput(
    var id: Long = 0,
    var name: String = "",
    var description: String = "",
    var endDate: String = "",
    var done: Boolean = false,
    var subTodoList: List<SubTodoItemShow> = listOf()
)

data class TodoItemDTOFinishTodo(
    val todoDetail: TodoItemDTODetail,
    val forceSave: Boolean,
)

data class TodoItemDTODetail(
    var todoItemOutput: TodoItemDTOOutput? = null,
    var subTodoList: MutableList<SubTodoItemShow>? = null
) {
}