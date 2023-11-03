package dev.vinicius.todoapp.data.local.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.vinicius.todoapp.data.local.AppDatabase
import dev.vinicius.todoapp.util.Converters
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    private val MIGRATIONS = arrayOf(
        AppDatabase.MIGRATION_1_2,
        AppDatabase.MIGRATION_2_3,
        AppDatabase.MIGRATION_3_4,
        AppDatabase.MIGRATION_4_5
    )

    @Provides
    fun provideChannelDao(appDatabase: AppDatabase) = appDatabase.todoItemDao()

    @Provides
    fun provideSubTodoDao(appDatabase: AppDatabase) = appDatabase.subTodoDao()
@Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase =
        Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "TodoDatabase"
        )
            .fallbackToDestructiveMigration()
            .addMigrations(*MIGRATIONS)
            .addTypeConverter(Converters())
            .build()

}