package dev.vinicius.todoapp.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import dev.vinicius.todoapp.data.model.TodoItem

class DiffCallback : DiffUtil.ItemCallback<TodoItem>(){
    override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem) =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem) =
        oldItem.name == newItem.name
}