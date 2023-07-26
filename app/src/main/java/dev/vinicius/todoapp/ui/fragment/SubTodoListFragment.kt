package dev.vinicius.todoapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dev.vinicius.todoapp.databinding.FragmentSubTodoListBinding
import dev.vinicius.todoapp.domain.dto.SubTodoItemShow
import dev.vinicius.todoapp.ui.adapter.SubTodoItemAdapter
import dev.vinicius.todoapp.viewmodel.CreateTodoViewModel

// Not used. But analyse a way to use this fragment on create and detail
class SubTodoListFragment: Fragment() {

    private lateinit var binding:FragmentSubTodoListBinding

    private val adapter by lazy { SubTodoItemAdapter() }

    var subTodoList: MutableList<SubTodoItemShow> = mutableListOf()
    var handleOnAddClick: (SubTodoItemShow) -> (Unit) = {}
    var handleOnDeleteClick: (SubTodoItemShow) -> (Unit) = {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentSubTodoListBinding.inflate(inflater, container, false)
        binding.rvSubTodoList.adapter = adapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvSubTodoList.layoutManager = layoutManager
        binding.lifecycleOwner = this


        setupListener()

        return binding.root
    }

    private fun setupListener(){
        binding.mbSubTodoAdd.setOnClickListener {
            val x = SubTodoItemShow(0, "TESTE", false)
            Snackbar.make(requireView(), "CLICADO", Snackbar.LENGTH_SHORT).show()
            handleOnAddClick(x)
            adapter.submitList(subTodoList)
        }

    }

//    fun addSubTodo(v: View){
//        val x = SubTodoItemShow(0, "TESTE", false)
//        Log.d("ADD CLICKED", x.toString())
//        Snackbar.make(requireView(), "CLICADO", Snackbar.LENGTH_SHORT).show()
//        handleOnAddClick(x)
//        adapter.submitList(subTodoList)
//    }
}