package dev.vinicius.todoapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.vinicius.todoapp.databinding.SubTodoListItemBinding
import dev.vinicius.todoapp.domain.dto.SubTodoItemInput

//TODO: Review necessity of this adapter
class SubTodoItemInputAdapter : ListAdapter<SubTodoItemInput, SubTodoItemInputAdapter.ViewHolder>(SubTodoInputDiffCallback()){

    inner class ViewHolder(
        private val binding: SubTodoListItemBinding
    ) : RecyclerView.ViewHolder(binding.root){
        fun bind(item:SubTodoItemInput){
//            binding.subTodoItem = item
//            binding.subTodoItemViewHolder = this
        }
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

class SubTodoInputDiffCallback: DiffUtil.ItemCallback<SubTodoItemInput>(){
    override fun areItemsTheSame(oldItem: SubTodoItemInput, newItem: SubTodoItemInput) =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: SubTodoItemInput, newItem: SubTodoItemInput) =
        oldItem.name == newItem.name

}