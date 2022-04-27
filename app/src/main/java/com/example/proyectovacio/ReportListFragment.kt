package com.example.proyectovacio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectovacio.databinding.FragmentDescripcionBinding
import com.example.proyectovacio.databinding.FragmentReportListBinding

class ReportListFragment : Fragment() {

    private lateinit var binding: FragmentReportListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_report_list,container,false)
        binding.lifecycleOwner = this
        val view = binding.root

        val reporteList: RecyclerView = binding.list // (1)

        val articleAdapter = ReporteAdapter { reporte -> onItemClick(reporte, view) } // (2)
        reporteList.adapter = articleAdapter // (3)

        articleAdapter.reportes = Report.data // (4)
        return view
    }

    private fun onItemClick(reporte: Report.Reporte, view: View) {
        Toast.makeText(context, reporte.nombre, Toast.LENGTH_SHORT).show()
        view.findNavController().navigate(R.id.ReportItemFragment)
    }
}