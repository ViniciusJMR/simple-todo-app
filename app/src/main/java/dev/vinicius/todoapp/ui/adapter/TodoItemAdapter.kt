package dev.vinicius.todoapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.vinicius.todoapp.databinding.TodoListItemBinding
import dev.vinicius.todoapp.domain.dto.TodoItemDTOOutput

class TodoItemAdapter() : ListAdapter<TodoItemDTOOutput, TodoItemAdapter.ViewHolder>(TodoDiffCallback()) {

    var onClickListener : (Long) -> (Unit) = {}

    inner class ViewHolder(
        private val binding: TodoListItemBinding
    ) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: TodoItemDTOOutput){
            binding.todoItem = item
            binding.todoItemViewHolder = this
        }

        fun onClick(v: View){
            onClickListener(binding.todoItem!!.id)
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

class TodoDiffCallback : DiffUtil.ItemCallback<TodoItemDTOOutput>(){
    override fun areItemsTheSame(oldItem: TodoItemDTOOutput, newItem: TodoItemDTOOutput) =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: TodoItemDTOOutput, newItem: TodoItemDTOOutput) =
        oldItem.name == newItem.name
}
