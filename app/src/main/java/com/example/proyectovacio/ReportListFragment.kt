package com.example.proyectovacio

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
import com.example.proyectovacio.databinding.FragmentReportListBinding

class ReportListFragment : Fragment() {

    private lateinit var binding: FragmentReportListBinding
    private val model: MyViewModel by navGraphViewModels(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_report_list,container,false)
        binding.lifecycleOwner = this
        binding.model = model

        val view = binding.root

        val reporteList: RecyclerView = binding.list

        val reporteAdapter = ReporteAdapter { reporte -> onItemClick(reporte, view) }
        reporteList.adapter = reporteAdapter

        reporteAdapter.reportes = Report.data
        return view
    }

    private fun onItemClick(reporte: Report.Reporte, view: View) {
        Toast.makeText(context, reporte.nombre, Toast.LENGTH_SHORT).show()
        model.setReportDetail(reporte)
        view.findNavController().navigate(R.id.action_ReportListFragment_to_ReportItemFragment)
    }
}