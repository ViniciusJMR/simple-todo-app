package dev.vinicius.todoapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import dev.vinicius.todoapp.data.model.SubTodoItem

@Dao
interface SubTodoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(subTodoItem: SubTodoItem): SubTodoItem

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(subTodoList: List<SubTodoItem>)

    @Delete
    suspend fun delete(subTodoItem:SubTodoItem)

    @Query("SELECT * FROM SubTodoItem WHERE id=:id")
    suspend fun getById(id: Long): SubTodoItem
}