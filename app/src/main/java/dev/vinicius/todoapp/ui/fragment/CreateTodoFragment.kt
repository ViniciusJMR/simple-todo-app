package dev.vinicius.todoapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.vinicius.todoapp.data.model.TodoItem
import dev.vinicius.todoapp.databinding.FragmentCreateTodoBinding
import dev.vinicius.todoapp.domain.dto.TodoItemDTOInput
import dev.vinicius.todoapp.util.State
import dev.vinicius.todoapp.viewmodel.CreateTodoViewModel

@AndroidEntryPoint
class CreateTodoFragment : Fragment() {
    private lateinit var binding: FragmentCreateTodoBinding

    private val createTodoViewModel by viewModels<CreateTodoViewModel> ()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateTodoBinding.inflate(inflater, container, false)
        binding.todoItem = TodoItemDTOInput()
        binding.fragment = this

        setupObservers()
        return binding.root
    }
    private fun setupObservers(){
        createTodoViewModel.state.observe(viewLifecycleOwner){
            when(it){
                is State.Loading -> {
                    view?.let {  it1 -> {
                        Snackbar.make(it1, "Carregando", Snackbar.LENGTH_SHORT).show()
                    }}
                }
                is State.Error ->{
                    view?.let{ it1 ->{
                        Snackbar.make(it1, it.error.message ?: "Erro desconhecido", Snackbar.LENGTH_SHORT).show()
                    }}
                }
                is State.Success ->{
                    view?.let{ it1 ->{
                        Snackbar.make(it1, "Salvo", Snackbar.LENGTH_SHORT).show()
                    }}
                    findNavController().navigateUp()
                    findNavController().popBackStack()
                }
            }
        }
    }

    fun saveTodo(v:View){
        createTodoViewModel.save(binding.todoItem!!)
    }
}