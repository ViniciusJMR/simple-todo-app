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

    suspend fun getById(id:Long) = flow {
        emit(todoItemDao.getById(id))
    }

    override suspend fun getAll() = flow {
        val todoItems = todoItemDao.getAll()
        emit(todoItems)
    }

    suspend fun getAllWithSubTodos() = flow {
        val todoItems = todoItemDao.getAllTodoWithSubTodos()
        emit(todoItems)
    }

    suspend fun getDetailTodoById(id: Long) = flow {
        val todoDetail = todoItemDao.getTodoWithSubTodos(id)
        emit(todoDetail)
    }

    override suspend fun insert(item: TodoItem) = flow {
        val id = todoItemDao.insert(item)
        emit(id)
    }

    override suspend fun update(item: TodoItem) = flow {
        todoItemDao.update(item.id, item.name, item.description, item.endDate)

        emit(Unit)
    }


    override suspend fun delete(item: TodoItem) = flow {
        todoItemDao.delete(item)
        emit(Unit)
    }
}