package dev.vinicius.todoapp.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.vinicius.todoapp.R
import dev.vinicius.todoapp.databinding.FragmentCreateTodoBinding
import dev.vinicius.todoapp.domain.dto.SubTodoItemShow
import dev.vinicius.todoapp.exception.TodoException
import dev.vinicius.todoapp.exception.field.FieldError
import dev.vinicius.todoapp.ui.adapter.SubTodoItemAdapter
import dev.vinicius.todoapp.ui.component.Dialogs
import dev.vinicius.todoapp.util.State
import dev.vinicius.todoapp.viewmodel.CreateTodoViewModel

@AndroidEntryPoint
class CreateTodoFragment : Fragment() {
    companion object {
        private val TAG = "CREATE TODO FRAGMENT"
    }

    private lateinit var binding: FragmentCreateTodoBinding
    private val adapter by lazy {
        SubTodoItemAdapter().apply {
            handleOnDeleteClick = {
                createTodoViewModel.deleteSubTodo(it)
            }

            handleOnClick = { subTodo, position ->
                setupDialog(subTodo.name) {
                    val list = createTodoViewModel.getSubTodoList()
                    val item = list?.get(position)
                    item?.name = it.text.toString()
                    notifyItemChanged(position)
                }
            }
        }
    }

    private val createTodoViewModel by viewModels<CreateTodoViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateTodoBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner


//        val subTodoListFragment = SubTodoListFragment()

//        subTodoListFragment.apply {
//            subTodoList = createTodoViewModel.todoItem.listSubTodoItem
//
//            handleOnAddClick = { subTodo ->
//                createTodoViewModel.addSubTodo(subTodo)
//                Log.d("LISTENER", subTodo.toString())
//                Log.d("LIST", createTodoViewModel.todoItem.listSubTodoItem.toString())
//            }
//            handleOnDeleteClick = { subTodo ->
//                createTodoViewModel.deleteSubTodo(subTodo)
//            }
//        }
//
//        val transaction = childFragmentManager.beginTransaction()
//        transaction.replace(R.id.fcv_create_todo_list, subTodoListFragment)

        setupUI()
        setupObservers()
        setupListener()
        return binding.root
    }

    private fun setupObservers() {
        createTodoViewModel.state.observe(viewLifecycleOwner) {
            clearInputError()
            when (it) {
                is State.Loading -> {
                }
                is State.Error -> {
                    if (it.error is TodoException)
                        processInputError(it.error.fields)

                    else
                        view?.let { it1 ->
                                Snackbar.make(
                                    it1,
                                    it.error.message ?: "Erro desconhecido",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                        }
                }

                is State.Success -> {
                    view?.let { it1 ->
                        {
                            Snackbar.make(it1, "Salvo", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                    findNavController().navigateUp()
                }
            }
        }
    }

    private fun setupUI() {
        binding.todoItem = createTodoViewModel.todoItem.value
        binding.fragment = this
        binding.rvCreateSubTodoList.adapter = adapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvCreateSubTodoList.layoutManager = layoutManager
    }

    private fun setupListener() {
        binding.mtbCreateTopBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.tilCreateAddSubTodo.setEndIconOnClickListener {
            val name = binding.tilCreateAddSubTodo.editText?.text.toString()
            createTodoViewModel.addSubTodo(name)
            adapter.addSubTodo(createTodoViewModel.getSubTodoList())
            binding.tilCreateAddSubTodo.editText?.setText("")
        }

        binding.tilCreateTodoEndDate.setStartIconOnClickListener {
            setupDatePicker(it)
        }
    }

    fun setupDatePicker(v: View) {
        val onPositive: (String) -> Unit = { date ->
            binding.edCreateTodoEndDate.setText(date)
        }

        val onNegative: () -> Unit = {
            binding.edCreateTodoEndDate.setText("")
        }

        Dialogs.setupDatePickerDialog(parentFragmentManager, onPositive, onNegative)
    }

    private fun setupDialog(textOnEditText: String, onChange: (EditText) -> (Unit)) {
        Dialogs.setupEditDialog(activity, context, textOnEditText, onChange)
    }

    fun saveTodo(v: View) {
        createTodoViewModel.save()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addSubTodo(v: View) {
//        createTodoViewModel.addSubTodo(SubTodoItemShow(name="TESTE", done=false))
//        setupDialog(""){editText ->
//            val text =  editText.text.toString()
//            createTodoViewModel.addSubTodo(SubTodoItemShow(name=text, done=false))
//            Log.d("DIALOG", "NEW DIALOG -> $text")
//            adapter.addSubTodo(createTodoViewModel.getSubTodoList())
//        }
    }

    private fun processInputError(fields: Map<String, FieldError>?){

        val inputErrorMap = hashMapOf(
            "name" to binding.tvCreateTiName,
            "date" to binding.tilCreateTodoEndDate,
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
        binding.tvCreateTiName.error = ""
        binding.tilCreateTodoEndDate.error = ""
    }


}