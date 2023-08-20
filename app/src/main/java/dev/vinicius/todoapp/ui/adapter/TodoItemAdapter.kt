package dev.vinicius.todoapp.ui.adapter

import android.content.Context
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.vinicius.todoapp.databinding.TodoListItemBinding
import dev.vinicius.todoapp.domain.dto.SubTodoItemShow
import dev.vinicius.todoapp.domain.dto.TodoItemDTODetail
import dev.vinicius.todoapp.domain.dto.TodoItemDTOOutput

class TodoItemAdapter(
    private val context: Context
) : ListAdapter<TodoItemDTODetail, TodoItemAdapter.ViewHolder>(TodoDiffCallback()) {

    var onClickListener: (Long) -> (Unit) = {}

    var onShowSubTodosListener: (Long) -> (Unit) = {}


    inner class ViewHolder(
        private val binding: TodoListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private val adapter by lazy {
            SubTodoItemAdapter()
                .apply {
                    options = { binding ->
                        binding.mcbSubTodo.visibility = View.GONE
                        binding.mbSubTodoItemDelete.visibility = View.GONE
                    }
                }
        }

        private var showingSubTodos = false

        fun bind(item: TodoItemDTODetail) {
            binding.todoItem = item.todoItemOutput
            binding.todoItemViewHolder = this
            binding.rvTodoListSubTodo.adapter = adapter
            binding.rvTodoListSubTodo.layoutManager =
                LinearLayoutManager(
                    context,
                    LinearLayoutManager.VERTICAL,
                    false
                )

            binding.cpiTodoCompletion.progress = getSubTodoProgress(item.subTodoList)

            val filteredList = item.subTodoList?.filter { !it.done }

            if (filteredList?.isNotEmpty() == true) {
                binding.mbTodoShow.visibility = View.VISIBLE
                adapter.submitList(filteredList)
            }

        }

        fun onClick(v: View) {
            onClickListener(binding.todoItem!!.id)
        }

        fun onShow(v: View) {
            onShowSubTodosListener(binding.todoItem!!.id)

            val visibility =
                if (showingSubTodos) {
                    View.GONE
                } else {
                    View.VISIBLE
                }

            showingSubTodos = !showingSubTodos
            binding.rvTodoListSubTodo.visibility = visibility
            binding.tvOnGoingSubTodoTitle.visibility = visibility

        }

        private fun getSubTodoProgress(list: MutableList<SubTodoItemShow>?): Int {
            val ONE_HUNDRED_PERCENT = 100.0
            val count = list!!.filter { it.done }.size

            if (count == 0)
                return 0

            val percentage = kotlin.math.ceil(ONE_HUNDRED_PERCENT / list.size * count)
            return percentage.toInt()
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

class TodoDiffCallback : DiffUtil.ItemCallback<TodoItemDTODetail>() {
    override fun areItemsTheSame(oldItem: TodoItemDTODetail, newItem: TodoItemDTODetail) =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: TodoItemDTODetail, newItem: TodoItemDTODetail) =
        oldItem.todoItemOutput?.name == newItem.todoItemOutput?.name
}
