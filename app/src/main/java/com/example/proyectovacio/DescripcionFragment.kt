package com.example.proyectovacio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.proyectovacio.databinding.FragmentConcursoListBinding
import com.example.proyectovacio.databinding.FragmentDescripcionBinding

class DescripcionFragment : Fragment() {

    lateinit var arrpescadesc: Array<String>
    private lateinit var binding: FragmentDescripcionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_descripcion,container,false)
        binding.lifecycleOwner = this
        val view = binding.root

        arrpescadesc = resources.getStringArray(R.array.descripcionesTiposDePesca)
        return view
    }

    fun cambiarModoPesca(index: Int) {
        binding.descripcionTextView.text = arrpescadesc[index]
    }



}