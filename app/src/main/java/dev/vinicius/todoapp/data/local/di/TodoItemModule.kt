package dev.vinicius.todoapp.data.local.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dev.vinicius.todoapp.data.local.repository.Repository
import dev.vinicius.todoapp.data.local.repository.impl.TodoItemRepository
import dev.vinicius.todoapp.data.model.TodoItem

@InstallIn(ViewModelComponent::class)
@Module
abstract class TodoItemModule {

    @Binds
    abstract fun getTodoItemSource(repo: TodoItemRepository): Repository<TodoItem>
}