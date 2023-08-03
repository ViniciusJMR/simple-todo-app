package dev.vinicius.todoapp.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.vinicius.todoapp.R
import dev.vinicius.todoapp.databinding.FragmentDetailTodoBinding
import dev.vinicius.todoapp.domain.dto.SubTodoItemShow
import dev.vinicius.todoapp.ui.adapter.SubTodoItemAdapter
import dev.vinicius.todoapp.util.State
import dev.vinicius.todoapp.viewmodel.DetailTodoViewModel
import dev.vinicius.todoapp.viewmodel.SharedViewModel

@AndroidEntryPoint
class DetailTodoFragment : Fragment() {

    private lateinit var binding: FragmentDetailTodoBinding
    private val detailTodoViewModel by viewModels<DetailTodoViewModel>()
    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private val adapter by lazy {
        SubTodoItemAdapter().apply {
            handleOnDeleteClick =  {
                detailTodoViewModel.deleteSubTodo(it)
            }

            handleOnClick = { subTodo, position ->
                setupDialog(subTodo.name){
                    val list = detailTodoViewModel.getSubTodoList()
                    val item = list?.get(position)
                    item?.name = it.text.toString()
                    detailTodoViewModel.updateSubTodo(item!!)
                }
            }

            handleOnCheckBoxClick = { subTodo, position ->
                val list = detailTodoViewModel.getSubTodoList()
                val item = list?.get(position)
                item?.done = subTodo.done
                detailTodoViewModel.updateSubTodo(item!!)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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

        binding.fragment = this
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupObserver() {
        detailTodoViewModel.stateTodo.observe(viewLifecycleOwner) {
            when (it) {
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
                    binding.todoItem = it.response
                }
            }
        }

        detailTodoViewModel.stateSubTodo.observe(viewLifecycleOwner) {
            when (it) {
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
                    adapter.submitList(it.response)
                    adapter.notifyDataSetChanged()

                    binding.cpiDetailSubTodoCompletion
                        .progress = detailTodoViewModel.getSubTodoProgress()
                }
            }
        }
    }

    private fun setupDialog(textOnEditText: String, onChange: (EditText) -> (Unit)){
        val editText = EditText(activity)
        editText.setText(textOnEditText)
        context?.let {
            val dialog = MaterialAlertDialogBuilder(it)
                .setTitle(R.string.txt_new_sub_todo_label)
                .setView(editText)
                .setPositiveButton("OK") { _, _ ->
                    onChange(editText)
                }
                .setNegativeButton("Cancel", null)
                .create()

            dialog.show()
        }
    }

    fun addSubTodo(v: View){
        setupDialog("") { editText ->
            val text = editText.text.toString()
            detailTodoViewModel.addSubTodo(SubTodoItemShow(name = text, done = false ))
        }
    }
}