package com.example.proyectovacio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.proyectovacio.databinding.FragmentDescripcionBinding
import com.example.proyectovacio.databinding.FragmentReportItemBinding

class ReportItemFragment: Fragment() {

    private lateinit var binding: FragmentReportItemBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_report_item,container,false)
        binding.lifecycleOwner = this
        val view = binding.root

        return view
    }
}

