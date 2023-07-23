package dev.vinicius.todoapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.vinicius.todoapp.data.model.TodoItem
import dev.vinicius.todoapp.data.model.TodoWithSubTodos

@Dao
interface TodoItemDao  {

    @Query("SELECT * FROM TodoItem")
    suspend fun getAll(): List<TodoItem>

    @Query("SELECT * FROM TodoItem WHERE id=:todoId")
    suspend fun getTodoWithSubTodos(todoId: Long): TodoWithSubTodos

    @Insert(onConflict= OnConflictStrategy.REPLACE)
    suspend fun insert(newTodo: TodoItem)

    @Delete
    suspend fun delete(todo: TodoItem)

}