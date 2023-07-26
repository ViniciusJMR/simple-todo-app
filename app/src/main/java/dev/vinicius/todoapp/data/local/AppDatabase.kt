package dev.vinicius.todoapp.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import dev.vinicius.todoapp.data.local.dao.SubTodoDao
import dev.vinicius.todoapp.data.local.dao.TodoItemDao
import dev.vinicius.todoapp.data.model.SubTodoItem
import dev.vinicius.todoapp.data.model.TodoItem
import dev.vinicius.todoapp.util.Converters

@Database(
    entities = [TodoItem::class, SubTodoItem::class],
    version = 4
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun todoItemDao(): TodoItemDao
    abstract fun subTodoDao(): SubTodoDao

    companion object{
        val MIGRATION_1_2 = Migration(1,2) {
            it.execSQL(
                "ALTER TABLE TodoItem ADD COLUMN endDate TEXT"
            )
        }
        val MIGRATION_2_3 = Migration(2,3) {
            it.execSQL(
                "CREATE TABLE IF NOT EXISTS SubTodoItem (" +
                        "id INTEGER NOT NULL," +
                        "parentTodoId INTEGER NOT NULL," +
                        "name TEXT NOT NULL," +
                        "done INTEGER NOT NULL," +
                        "PRIMARY KEY(id)" +
                        "FOREIGN KEY (parentTodoId) REFERENCES TodoItem(id)" +
                        "ON DELETE CASCADE" +
                        ")"
            )
        }

        val MIGRATION_3_4 = Migration(3,4) {
            it.execSQL(
                "CREATE TABLE IF NOT EXISTS SubTodoItemAux (" +
                        "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                        "parentTodoId INTEGER NOT NULL," +
                        "name TEXT NOT NULL," +
                        "done INTEGER NOT NULL," +
                        "FOREIGN KEY (parentTodoId) REFERENCES TodoItem(id)" +
                        "ON DELETE CASCADE" +
                        ")"
            )

            it.execSQL(
                "INSERT INTO SubTodoItemAux(id, parentTodoId, name, done) " +
                    " SELECT id, parentTodoId, name, done FROM SubTodoItem"
            )

            it.execSQL(
                "DROP TABLE SubTodoItem"
            )
            it.execSQL(
                "ALTER TABLE SubTodoItemAux RENAME TO SubTodoItem"
            )
        }
    }
}