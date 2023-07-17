package dev.vinicius.todoapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import dev.vinicius.todoapp.data.local.dao.TodoItemDao
import dev.vinicius.todoapp.data.model.TodoItem
import dev.vinicius.todoapp.util.Converters

@Database(
    entities = [TodoItem::class],
    version = 2
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun todoItemDao(): TodoItemDao

    companion object{
        val MIGRATION_1_2 = Migration(1,2) {
            it.execSQL(
                "ALTER TABLE TodoItem ADD COLUMN endDate TEXT"
            )
        }
    }
}