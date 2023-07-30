package dev.vinicius.todoapp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.vinicius.todoapp.databinding.SubTodoListItemBinding
import dev.vinicius.todoapp.domain.dto.SubTodoItemShow

class SubTodoItemAdapter : ListAdapter<SubTodoItemShow, SubTodoItemAdapter.ViewHolder>(SubTodoDiffCallback()){

    var handleOnDeleteClick: (SubTodoItemShow) -> (Unit) = {}

    var handleOnClick: (SubTodoItemShow, Int) -> Unit = { subTodo, i -> }

    inner class ViewHolder(
        private val binding: SubTodoListItemBinding
    ) : RecyclerView.ViewHolder(binding.root){
        fun bind(item:SubTodoItemShow){
            binding.subTodoItem = item
            binding.subTodoItemViewHolder = this
        }

        fun onDelete(v: View){
            handleOnDeleteClick(getItem(adapterPosition))
            notifyItemRemoved(adapterPosition)
        }

        fun onClick(v: View){
            handleOnClick(getItem(adapterPosition), adapterPosition)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addSubTodo(list: List<SubTodoItemShow>?){
        submitList(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SubTodoListItemBinding.inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class SubTodoDiffCallback: DiffUtil.ItemCallback<SubTodoItemShow>(){
    override fun areItemsTheSame(oldItem: SubTodoItemShow, newItem: SubTodoItemShow) =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: SubTodoItemShow, newItem: SubTodoItemShow) =
        oldItem.name == newItem.name

}