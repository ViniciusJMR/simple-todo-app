package dev.vinicius.todoapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
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
import dev.vinicius.todoapp.core.hideSoftKeyboard
import dev.vinicius.todoapp.databinding.FragmentMainBinding
import dev.vinicius.todoapp.exception.SubTodoException
import dev.vinicius.todoapp.exception.TodoException
import dev.vinicius.todoapp.ui.adapter.TodoItemAdapter
import dev.vinicius.todoapp.ui.component.Dialogs
import dev.vinicius.todoapp.util.State
import dev.vinicius.todoapp.viewmodel.SharedViewModel
import dev.vinicius.todoapp.viewmodel.TodoItemViewModel


@AndroidEntryPoint
class MainFragment : Fragment(){

    private lateinit var binding: FragmentMainBinding
    private val todoItemViewModel by viewModels<TodoItemViewModel>()
    private val sharedViewModel by activityViewModels<SharedViewModel>()

    private val adapter by lazy {
        TodoItemAdapter(requireContext()).apply {
            onClickListener = { id ->
                sharedViewModel.selectItem(id)
                findNavController().navigate(R.id.action_mainFragment_to_detailTodoFragment)
            }

            handleOnCheckBoxClick = { item, position ->
                todoItemViewModel.updateTodoDone(item)
            }

//            handleOnCheckBoxLongClick = { item, position ->
//                todoItemViewModel.updateTodoDone(item, true)
//            }
        }
    }

    fun goToCreateTodo(v: View) {
        findNavController().navigate(R.id.action_mainFragment_to_createTodoFragment)
    }

    fun goToEditTodo() {
        findNavController().navigate(R.id.action_mainFragment_to_editTodoFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        setupUI()
        setupListener()
        setupObserver()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        todoItemViewModel.getAll()
    }

    private fun setupListener() {
        binding.mtbTopBar.setNavigationOnClickListener {

        }

        binding.mtbTopBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.main_menu_search -> {
                    val queryTextListener = object: SearchView.OnQueryTextListener{
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            Log.d(TAG, query.toString())
                            todoItemViewModel.filterTodoList(query ?: "")
                            binding.root.hideSoftKeyboard()
                            return true
                        }

                        override fun onQueryTextChange(newText: String?): Boolean {
                            Log.d(TAG, newText.toString())
                            todoItemViewModel.filterTodoList(newText ?: "")
                            return true
                        }
                    }
                    (menuItem.actionView as SearchView).setOnQueryTextListener(queryTextListener)

                    true
                }
                else -> false
            }
        }

    }

    private fun setupUI() {
        binding.rvTodoList.adapter = adapter
        binding.rvTodoList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvTodoList.itemAnimator?.changeDuration = 0

        binding.fragment = this

        setupSwipe()
    }

    private fun setupSwipe() {
        val rightSwiperHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
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

        val leftSwipeHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val todoDetail = adapter.currentList[viewHolder.adapterPosition]
                sharedViewModel.selectItem(todoDetail.todoItemOutput!!)
                goToEditTodo()
            }

        })

        rightSwiperHelper.attachToRecyclerView(binding.rvTodoList)
        leftSwipeHelper.attachToRecyclerView(binding.rvTodoList)

    }

    private fun setupObserver() {
        todoItemViewModel.todoList.observe(viewLifecycleOwner) {
            when (it) {
                is State.Success -> {
                    adapter.submitList(it.response)
                    adapter.notifyDataSetChanged()
                }

                is State.Error -> {
                    view?.let { it1 ->
                        Snackbar.make(
                            it1,
                            it.error.message ?: "Erro",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }

                is State.Loading -> {
                }
            }
        }

        todoItemViewModel.state.observe(viewLifecycleOwner) {
            when(it) {
                is State.Error -> {
                    Log.d(TAG, "state observe: ${it.error.message}")
                    adapter.notifyDataSetChanged()
                    if (it.error is SubTodoException) {
                        Dialogs.setupDialog(
                            context,
                            "${it.error.message}. Hold the checkbox to force save.",
                        ){}
                    }
                }
                else -> {}
            }
        }

//        todoItemViewModel.filteredTodoList.observe(viewLifecycleOwner) {
//            adapter.submitList(it)
//            adapter.notifyDataSetChanged()
//        }
    }


    companion object {
        private val TAG = "MAIN_FRAGMENT"
    }
}