package com.example.proyectovacio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.proyectovacio.databinding.FragmentReportItemBinding

class ReportItemFragment: Fragment() {

    private var _binding: FragmentReportItemBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportItemBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }
}

