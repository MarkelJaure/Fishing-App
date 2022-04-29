package com.example.proyectovacio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectovacio.databinding.FragmentReglamentacionListBinding

class ReglamentacionListFragment : Fragment() {

    private lateinit var binding: FragmentReglamentacionListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_reglamentacion_list,container,false)
        binding.lifecycleOwner = this
        val view = binding.root

        val reglamentacionList: RecyclerView = binding.list

        val reglamentacionAdapter = ReglamentacionAdapter()
        reglamentacionList.adapter = reglamentacionAdapter
        reglamentacionAdapter.reglamentaciones = Reglamentacion.data

        return view
    }
}