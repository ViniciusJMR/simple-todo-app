package dev.vinicius.todoapp.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import dev.vinicius.todoapp.domain.dto.TodoItemDTOOutput

class DiffCallback : DiffUtil.ItemCallback<TodoItemDTOOutput>(){
    override fun areItemsTheSame(oldItem: TodoItemDTOOutput, newItem: TodoItemDTOOutput) =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: TodoItemDTOOutput, newItem: TodoItemDTOOutput) =
        oldItem.name == newItem.name
}