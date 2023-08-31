package dev.vinicius.todoapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.vinicius.todoapp.data.model.TodoItem
import dev.vinicius.todoapp.data.model.TodoWithSubTodos
import java.time.LocalDate

@Dao
interface TodoItemDao  {

    @Query("SELECT * FROM TodoItem")
    suspend fun getAll(): List<TodoItem>

    @Query("SELECT * FROM TodoItem WHERE id=:id")
    suspend fun getById(id: Long): TodoItem

    @Query("SELECT * FROM TodoItem WHERE id=:todoId")
    suspend fun getTodoWithSubTodos(todoId: Long): TodoWithSubTodos

    @Query("SELECT * FROM TodoItem")
    suspend fun getAllTodoWithSubTodos(): List<TodoWithSubTodos>

    @Insert(onConflict= OnConflictStrategy.REPLACE)
    suspend fun insert(newTodo: TodoItem): Long
    
    @Query("UPDATE todoItem SET name=:name, description=:description, endDate=:endDate WHERE id=:id" )
    suspend fun update(id: Long, name: String, description: String, endDate: LocalDate?)

    @Delete
    suspend fun delete(todo: TodoItem)

}