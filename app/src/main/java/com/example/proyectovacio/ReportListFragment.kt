package com.example.proyectovacio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectovacio.databinding.FragmentAboutUsBinding
import com.example.proyectovacio.databinding.FragmentDescripcionBinding
import com.example.proyectovacio.databinding.FragmentReportListBinding

class ReportListFragment : Fragment() {

    private var _binding: FragmentReportListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportListBinding.inflate(inflater, container, false)
        val view = binding.root

        val reporteList: RecyclerView = binding.list // (1)

        val articleAdapter = ReporteAdapter() // (2)
        reporteList.adapter = articleAdapter // (3)

        articleAdapter.reportes = Report.data // (4)
        return view
    }


}