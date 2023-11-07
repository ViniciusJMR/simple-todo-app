package dev.vinicius.todoapp.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.vinicius.todoapp.R
import dev.vinicius.todoapp.core.hideSoftKeyboard
import dev.vinicius.todoapp.databinding.FragmentHistoryBinding
import dev.vinicius.todoapp.exception.SubTodoException
import dev.vinicius.todoapp.ui.adapter.TodoItemAdapter
import dev.vinicius.todoapp.ui.component.Dialogs
import dev.vinicius.todoapp.util.State
import dev.vinicius.todoapp.viewmodel.HistoryViewModel
import dev.vinicius.todoapp.viewmodel.SharedViewModel
import dev.vinicius.todoapp.viewmodel.TodoItemViewModel

@AndroidEntryPoint
class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private val historyViewModel by viewModels<HistoryViewModel>()
    private val sharedViewModel by activityViewModels<SharedViewModel>()

    private val adapter by lazy {
        TodoItemAdapter(requireContext()).apply {
            onClickListener = { id ->
                sharedViewModel.selectItem(id)
                findNavController().navigate(R.id.action_historyFragment_to_detailTodoFragment)
            }

            handleOnCheckBoxClick = { item, position ->
                historyViewModel.updateTodoDone(item)
            }

//            handleOnCheckBoxLongClick = { item, position ->
//                todoItemViewModel.updateTodoDone(item, true)
//            }
        }
    }

    companion object { private val TAG = "HistoryFragment"}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.fragment = this

        setupListeners()
        setupUI()
        setupObserver()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        historyViewModel.getAllNotFinished()
    }

    private fun setupListeners(){
        binding.mtbTopBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.mtbTopBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.main_menu_search -> {
                    val queryTextListener = object: SearchView.OnQueryTextListener{
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            Log.d(TAG, query.toString())
                            historyViewModel.filterTodoList(query ?: "")
                            binding.root.hideSoftKeyboard()
                            return true
                        }

                        override fun onQueryTextChange(newText: String?): Boolean {
                            Log.d(TAG, newText.toString())
                            historyViewModel.filterTodoList(newText ?: "")
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
        binding.rvHistoryTodos.adapter = adapter
        binding.rvHistoryTodos.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvHistoryTodos.itemAnimator?.changeDuration = 0

        binding.fragment = this
    }


    private fun setupObserver() {
        historyViewModel.todoList.observe(viewLifecycleOwner) {
            when (it) {
                is State.Success -> {
                    adapter.submitList(it.response)
                    adapter.notifyDataSetChanged()
                }

                is State.Error -> {
                    view?.let { v ->
                        Snackbar.make( v, it.error.message ?: "Erro", Snackbar.LENGTH_LONG )
                            .show()
                    }
                }

                is State.Loading -> {
                }
            }
        }

    }

}