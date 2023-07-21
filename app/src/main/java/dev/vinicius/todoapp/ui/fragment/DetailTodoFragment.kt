package dev.vinicius.todoapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.vinicius.todoapp.R
import dev.vinicius.todoapp.databinding.FragmentDetailTodoBinding

class DetailTodoFragment : Fragment() {

    private lateinit var binding: FragmentDetailTodoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailTodoBinding.inflate(inflater, container, false)

        return binding.root
    }
}