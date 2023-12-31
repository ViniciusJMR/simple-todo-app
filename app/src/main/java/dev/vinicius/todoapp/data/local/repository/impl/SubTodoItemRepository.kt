package dev.vinicius.todoapp.data.local.repository.impl

import dev.vinicius.todoapp.data.local.dao.SubTodoDao
import dev.vinicius.todoapp.data.local.repository.Repository
import dev.vinicius.todoapp.data.model.SubTodoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SubTodoItemRepository @Inject constructor(
    private val subTodoDao: SubTodoDao
): Repository<SubTodoItem>{
    override suspend fun insert(item: SubTodoItem) = flow {
        subTodoDao.insert(item)
        emit(1L)
    }

    suspend fun insertAll(subTodoList: List<SubTodoItem>) = flow {
        subTodoDao.insertAll(subTodoList)
        emit(Unit)
    }

    suspend fun insertReturnInserted(item: SubTodoItem) = flow {
        val id = subTodoDao.insert(item)
        val inserted = subTodoDao.getById(id)
        emit(inserted)
    }

    override suspend fun delete(item: SubTodoItem) = flow {
        subTodoDao.delete(item)
        emit(Unit)
    }

    override suspend fun getAll(): Flow<List<SubTodoItem>> = flow {
    }

    override suspend fun update(item: SubTodoItem) = flow {
        emit(Unit)
    }

    suspend fun getById(id: Long) = flow {
        val subTodo = subTodoDao.getById(id)
        emit(subTodo)
    }
}