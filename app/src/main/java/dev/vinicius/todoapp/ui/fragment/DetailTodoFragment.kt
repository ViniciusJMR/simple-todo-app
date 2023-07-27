package dev.vinicius.todoapp.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.createViewModelLazy
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.vinicius.todoapp.R
import dev.vinicius.todoapp.databinding.FragmentDetailTodoBinding
import dev.vinicius.todoapp.ui.adapter.SubTodoItemAdapter
import dev.vinicius.todoapp.util.State
import dev.vinicius.todoapp.viewmodel.DetailTodoViewModel
import dev.vinicius.todoapp.viewmodel.SharedViewModel

@AndroidEntryPoint
class DetailTodoFragment : Fragment() {

    private lateinit var binding: FragmentDetailTodoBinding
    private val detailTodoViewModel by viewModels<DetailTodoViewModel>()
    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private val adapter by lazy { SubTodoItemAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailTodoBinding.inflate(inflater, container, false)

        setupUI()
        setupObserver()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = sharedViewModel.getSelectedAsLong()
        detailTodoViewModel.getTodoDetail(id)
    }

    private fun setupUI(){
        binding.rvDetailSubTodoList.adapter = adapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvDetailSubTodoList.layoutManager = layoutManager
    }

    private fun setupObserver(){
        detailTodoViewModel.state.observe(viewLifecycleOwner){
            when(it){
                is State.Loading -> {}
                is State.Error -> {
                    Log.d("DETAIL", it.error.message.toString())
                    view?.let { views ->
                        Snackbar
                            .make(views, "Error: ${it.error.message}", Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }
                is State.Success -> {
                    binding.todoItem = it.response?.todoItemOutput
                    adapter.submitList(it.response?.subTodoList)
                }
            }
        }

    }
}