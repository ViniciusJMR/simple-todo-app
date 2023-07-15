package dev.vinicius.todoapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.vinicius.todoapp.data.model.TodoItem

@Dao
interface TodoItemDao  {

    @Query("SELECT * FROM TodoItem")
    suspend fun getAll(): List<TodoItem>

    @Insert(onConflict= OnConflictStrategy.IGNORE)
    suspend fun insert(newCard: TodoItem)

}