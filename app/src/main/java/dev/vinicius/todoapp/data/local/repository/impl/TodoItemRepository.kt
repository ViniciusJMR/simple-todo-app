package dev.vinicius.todoapp.data.local.repository.impl

import dev.vinicius.todoapp.data.local.dao.TodoItemDao
import dev.vinicius.todoapp.data.local.repository.Repository
import dev.vinicius.todoapp.data.model.TodoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TodoItemRepository @Inject constructor(
    private val todoItemDao: TodoItemDao
) : Repository<TodoItem> {
    override suspend fun getAll() = flow {
        val todoItems = todoItemDao.getAll()
        emit(todoItems)
    }

    override suspend fun insert(item: TodoItem) = flow {
        val id = todoItemDao.insert(item)
        emit(id)
    }

    override suspend fun delete(item: TodoItem) = flow {
        todoItemDao.delete(item)
        emit(Unit)
    }

}