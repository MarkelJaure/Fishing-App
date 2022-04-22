package com.example.proyectovacio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.proyectovacio.databinding.FragmentAboutUsBinding
import com.example.proyectovacio.databinding.FragmentDescripcionBinding


class AboutUsFragment : Fragment() {

    private var _binding: FragmentAboutUsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutUsBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }


}