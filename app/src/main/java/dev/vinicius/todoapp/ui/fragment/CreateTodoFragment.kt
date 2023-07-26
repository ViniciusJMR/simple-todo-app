package dev.vinicius.todoapp.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.vinicius.todoapp.databinding.FragmentCreateTodoBinding
import dev.vinicius.todoapp.domain.dto.SubTodoItemShow
import dev.vinicius.todoapp.ui.adapter.SubTodoItemAdapter
import dev.vinicius.todoapp.util.State
import dev.vinicius.todoapp.viewmodel.CreateTodoViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset

@AndroidEntryPoint
class CreateTodoFragment : Fragment() {
    private lateinit var binding: FragmentCreateTodoBinding
    private val adapter by lazy {
        SubTodoItemAdapter().apply {
            handleOnDeleteClick =  {
                createTodoViewModel.deleteSubTodo(it)
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
        return binding.root
    }

    private fun setupObservers() {
        createTodoViewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is State.Loading -> {}
                is State.Error -> {
                    view?.let { it1 ->
                        {
                            Snackbar.make(
                                it1,
                                it.error.message ?: "Erro desconhecido",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
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

    private fun setupUI(){
        binding.todoItem = createTodoViewModel.todoItem.value
        binding.fragment = this
        binding.rvCreateSubTodoList.adapter = adapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvCreateSubTodoList.layoutManager = layoutManager
    }

    fun setupDatePicker(v: View) {
        val datePicker = MaterialDatePicker.Builder.datePicker().build()
        datePicker.show(parentFragmentManager, "MATERIAL_DATE_PICKER")
        datePicker.addOnPositiveButtonClickListener { millisecondsDate ->
            val date = Instant.ofEpochMilli(millisecondsDate)
                .atZone(ZoneId.of("America/Sao_Paulo"))
                .withZoneSameInstant(ZoneId.ofOffset("UTC", ZoneOffset.UTC))
                .toLocalDate()
            binding.txtShowDateEdit.setText(date.toString())
        }
    }

    fun saveTodo(v: View) {
        createTodoViewModel.save(binding.todoItem!!)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addSubTodo(v: View) {
        createTodoViewModel.addSubTodo(SubTodoItemShow(name="TESTE", done=false))
        adapter.submitList(createTodoViewModel.getSubTodoList())
        adapter.notifyDataSetChanged()
    }
}