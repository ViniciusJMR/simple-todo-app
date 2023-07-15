package dev.vinicius.todoapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.vinicius.todoapp.databinding.TodoListItemBinding
import dev.vinicius.todoapp.data.model.TodoItem

class TodoItemAdapter() : ListAdapter<TodoItem, TodoItemAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(
        private val binding: TodoListItemBinding
    ) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: TodoItem){
            binding.todoItem = item
            binding.todoItemViewHolder = this
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TodoListItemBinding.inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}