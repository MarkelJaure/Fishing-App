package com.example.fishingapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.fishingapp.adapters.ConcursoAdapter
import com.example.fishingapp.databinding.FragmentConcursoListBinding
import com.example.fishingapp.models.Concurso
import com.example.fishingapp.viewModels.MyViewModel

class ConcursoListFragment : Fragment() {

    private lateinit var binding: FragmentConcursoListBinding
    private val model: MyViewModel by navGraphViewModels(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_concurso_list,container,false)
        binding.lifecycleOwner = this
        binding.model = model

        val view = binding.root

        val concursoList: RecyclerView = binding.list // (1)

        val articleAdapter = ConcursoAdapter { concurso -> onItemClick(concurso, view) } // (2)
        concursoList.adapter = articleAdapter // (3)

        articleAdapter.concursos = Concurso.data // (4)
        return view
    }
    private fun onItemClick(concurso: Concurso, view: View) {
        Toast.makeText(context, concurso.nombre, Toast.LENGTH_SHORT).show()
        model.setConcursoDetail(concurso)
        view.findNavController().navigate(R.id.action_ConcursoListFragment_to_ConcursoItemFragment)
    }
}