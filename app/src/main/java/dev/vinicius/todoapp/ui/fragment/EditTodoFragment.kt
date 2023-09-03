package dev.vinicius.todoapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.vinicius.todoapp.R
import dev.vinicius.todoapp.databinding.FragmentEditTodoBinding
import dev.vinicius.todoapp.domain.dto.TodoItemDTOInput
import dev.vinicius.todoapp.exception.TodoException
import dev.vinicius.todoapp.exception.field.FieldError
import dev.vinicius.todoapp.ui.component.Dialogs
import dev.vinicius.todoapp.util.State
import dev.vinicius.todoapp.viewmodel.EditTodoViewModel
import dev.vinicius.todoapp.viewmodel.SharedViewModel

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
        binding.lifecycleOwner = viewLifecycleOwner

        setupUI()
        setupListener()
        setupObservers()

        return binding.root
    }

    private fun setupObservers(){
        editTodoViewModel.updateState.observe(viewLifecycleOwner){
            clearInputError()
            when (it) {
                is State.Loading -> {}
                is State.Error -> {
                    if (it.error is TodoException)
                        processInputError(it.error.fields)
                    else
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
            todoOutput.getFormattedEndDate() ?: "",
        )
        binding.todoItem = todoInput
    }

    private fun setupListener() {
        binding.mtbEditTopBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.tilEditTodoEnddate.setStartIconOnClickListener {
            setupDatePicker(it)
        }
    }

    fun saveTodo(v: View) {
        editTodoViewModel.save(binding.todoItem!!)
    }

    fun setupDatePicker(v: View) {
        val onPositive: (String) -> Unit = { date ->
            binding.txtShowDateEdit.setText(date)
        }

        val onNegative: () -> Unit = {
            binding.txtShowDateEdit.setText("")
        }

        Dialogs.setupDatePickerDialog(parentFragmentManager, onPositive, onNegative)
    }

    private fun processInputError(fields: Map<String, FieldError>?){

        val inputErrorMap = hashMapOf(
            "name" to binding.tilEditTodoName,
            "date" to binding.tilEditTodoEnddate,
        )

        val processErrorMap = hashMapOf(
            FieldError.BLANKORNULL to getString(R.string.err_task_name),
            FieldError.DATEFORMAT to getString(R.string.err_task_date)
        )

        fields?.forEach {
            val inputError = it.key
            val inputTypeError = it.value

            inputErrorMap[inputError]?.error = processErrorMap[inputTypeError]
        }
    }

    private fun clearInputError(){
        binding.tilEditTodoName.error = ""
        binding.tilEditTodoEnddate.error = ""
    }
}