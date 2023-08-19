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
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.vinicius.todoapp.R
import dev.vinicius.todoapp.databinding.FragmentDetailTodoBinding
import dev.vinicius.todoapp.domain.dto.SubTodoItemShow
import dev.vinicius.todoapp.ui.adapter.SubTodoItemAdapter
import dev.vinicius.todoapp.ui.component.Dialogs
import dev.vinicius.todoapp.util.State
import dev.vinicius.todoapp.viewmodel.DetailTodoViewModel
import dev.vinicius.todoapp.viewmodel.SharedViewModel
import kotlin.math.absoluteValue

@AndroidEntryPoint
class DetailTodoFragment : Fragment() {

    private lateinit var binding: FragmentDetailTodoBinding
    private val detailTodoViewModel by viewModels<DetailTodoViewModel>()
    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private val adapter by lazy {
        SubTodoItemAdapter().apply {
            handleOnDeleteClick = {
                detailTodoViewModel.deleteSubTodo(it)
            }

            handleOnClick = { subTodo, position ->
                setupDialog(subTodo.name) {
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

    private fun setupUI() {
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
                    val todo = it.response
                    binding.todoItem = todo
                    binding.tvDetailDescription.text = todo?.description?.ifBlank {
                        getString(R.string.txt_no_description)
                    }


                    val daysLeft = todo?.getDaysLeft()
                    daysLeft?.let { days ->
                        binding.cDetailEndDate.apply {
                            visibility = View.VISIBLE
                            text = getDaysLeftResource(days)
                        }
                    }

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

    private fun setupDialog(textOnEditText: String, onChange: (EditText) -> (Unit)) {
        Dialogs.setupEditDialog(activity, context, textOnEditText, onChange)
    }

    private fun getDaysLeftResource(days: Int) =
        if (days == 1){
            getString(R.string.txt_ends_one_day)
        } else if (days == -1){
            getString(R.string.txt_passed_one_day)
        } else if (days > 0) {
            getString(R.string.txt_ends_n_days)
                .format(days)
        } else if (days < 0) {
            getString(R.string.txt_passed_n_days)
                .format(days.absoluteValue)
        } else {
            getString(R.string.txt_today)
        }

    fun handleOnClickEndDateClick(v: View){
        binding.todoItem?.let { todo ->
            var endDateText: String? = ""
            if(binding.cDetailEndDate.text == todo.getFormattedEndDate()) {
                val daysLeft = todo.getDaysLeft()
                daysLeft?.let { days ->
                    endDateText = getDaysLeftResource(days)
                }
            } else {
                endDateText = todo.getFormattedEndDate()
            }
            binding.cDetailEndDate.text = endDateText
        }
    }

    fun addSubTodo(v: View) {
        setupDialog("") { editText ->
            val text = editText.text.toString()
            detailTodoViewModel.addSubTodo(SubTodoItemShow(name = text, done = false))
        }
    }
}