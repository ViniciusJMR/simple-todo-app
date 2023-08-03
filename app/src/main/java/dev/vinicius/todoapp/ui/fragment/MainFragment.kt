package dev.vinicius.todoapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.vinicius.todoapp.R
import dev.vinicius.todoapp.databinding.FragmentMainBinding
import dev.vinicius.todoapp.ui.adapter.TodoItemAdapter
import dev.vinicius.todoapp.util.State
import dev.vinicius.todoapp.viewmodel.SharedViewModel
import dev.vinicius.todoapp.viewmodel.TodoItemViewModel


@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val todoItemViewModel by viewModels<TodoItemViewModel>()
    private val sharedViewModel by activityViewModels<SharedViewModel>()

    private val adapter by lazy {
        TodoItemAdapter().apply {
            onClickListener = { id ->
                sharedViewModel.selectItem(id)
                findNavController().navigate(R.id.action_mainFragment_to_detailTodoFragment)
            }
        }
    }

    fun goToCreateTodo(v: View){
        findNavController().navigate(R.id.action_mainFragment_to_createTodoFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        setupUI()
        setupObserver()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        todoItemViewModel.getAll()
    }

    private fun setupUI(){
        binding.rvTodoList.adapter = adapter
        binding.rvTodoList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        binding.fragment = this

        val helper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(
                viewHolder: RecyclerView.ViewHolder,
                direction: Int
            ) {
                todoItemViewModel.deleteTodo(viewHolder.adapterPosition)
            }
        })

        helper.attachToRecyclerView(binding.rvTodoList)
    }

    private fun setupObserver(){
        todoItemViewModel.todoList.observe(viewLifecycleOwner) {
            when(it){
                is State.Success ->{
                    adapter.submitList(it.response)
                    adapter.notifyDataSetChanged()
                }
                is State.Error -> {
                    view?.let { it1 -> Snackbar.make(it1, it.error.message ?: "Erro", Snackbar.LENGTH_LONG).show() }
                }
                is State.Loading -> {
                }
            }
        }
    }
}