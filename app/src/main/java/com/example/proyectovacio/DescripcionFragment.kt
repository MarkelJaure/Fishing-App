package com.example.proyectovacio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.proyectovacio.databinding.FragmentDescripcionBinding

class DescripcionFragment : Fragment() {

    lateinit var arrpescadesc: Array<String>
    var modoindex = 0

    private var _binding: FragmentDescripcionBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDescripcionBinding.inflate(inflater, container, false)
        val view = binding.root

        arrpescadesc = resources.getStringArray(R.array.descripcionesTiposDePesca)
        return view
    }

    fun cambiarModoPesca(index: Int) {
        modoindex = index
        binding.descripcionTextView.text = arrpescadesc[modoindex]
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}