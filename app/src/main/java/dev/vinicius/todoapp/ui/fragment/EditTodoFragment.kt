package dev.vinicius.todoapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.vinicius.todoapp.R
import dev.vinicius.todoapp.databinding.FragmentEditTodoBinding
import dev.vinicius.todoapp.domain.dto.TodoItemDTOInput
import dev.vinicius.todoapp.ui.component.Dialogs
import dev.vinicius.todoapp.util.State
import dev.vinicius.todoapp.viewmodel.EditTodoViewModel
import dev.vinicius.todoapp.viewmodel.SharedViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset

@AndroidEntryPoint
class EditTodoFragment : Fragment() {

    private lateinit var binding: FragmentEditTodoBinding
    private val editTodoViewModel: EditTodoViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentEditTodoBinding.inflate(inflater, container, false)

        setupUI()
        setupObservers()

        return binding.root
    }

    private fun setupObservers(){
        editTodoViewModel.updateState.observe(viewLifecycleOwner){
            when (it) {
                is State.Loading -> {}
                is State.Error -> {
                    view?.let { view ->
                        Snackbar
                            .make(view, it.error.message ?: "Erro desconhecido", Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }
                is State.Success -> {
                    findNavController().navigateUp()
                }
            }
        }
    }

    private fun setupUI(){
        binding.fragment = this
        val todoOutput = sharedViewModel.getSelectedAsTodoItemOutput()
        // TODO: Change to livedata object on view model
        sharedViewModel.selectItem(todoOutput.id)
        val todoInput = TodoItemDTOInput(
            todoOutput.id,
            todoOutput.name,
            todoOutput.description,
            todoOutput.endDate.toString(),
        )
        binding.todoItem = todoInput
    }

    fun saveTodo(v: View) {
        editTodoViewModel.save(binding.todoItem!!)
    }

    fun setupDatePicker(v: View) {
        val onPositive: (LocalDate) -> Unit = { date ->
            binding.txtShowDateEdit.setText(date.toString())
        }

        Dialogs.setupDatePickerDialog(parentFragmentManager, onPositive)
    }
}