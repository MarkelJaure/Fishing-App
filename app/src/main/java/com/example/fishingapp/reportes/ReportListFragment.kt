package com.example.fishingapp.reportes

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.fishingapp.R
import com.example.fishingapp.adapters.ReporteAdapter
import com.example.fishingapp.databinding.FragmentReportListBinding
import com.example.fishingapp.models.Reporte
import com.example.fishingapp.viewModels.MyViewModel
import com.example.fishingapp.viewModels.ReporteViewModel


class ReportListFragment : Fragment() {

    private lateinit var binding: FragmentReportListBinding
    private val model: MyViewModel by navGraphViewModels(R.id.navigation)
    private val reporteModel: ReporteViewModel by navGraphViewModels(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_report_list,container,false)
        binding.lifecycleOwner = this
        binding.model = model

        val view = binding.root

        val reporteList: RecyclerView = binding.list

        val reporteAdapter = ReporteAdapter { reporte -> onItemClick(reporte, view) }
        reporteList.adapter = reporteAdapter

        reporteModel.allReportes.observe(viewLifecycleOwner) { reportes ->
            Log.w("reportes room", reportes.toString())
            reporteAdapter.reportes = reportes
        }

        binding.fab.setOnClickListener {
            model.setEditReport(false)
            model.setReportDetail(null)
            view.findNavController().navigate(R.id.formFragment)
        }
        //reporteAdapter.reportes = Reporte.data
        binding.toolBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.DateFilter -> {
                    Log.w("Item","ITEM 1")
                    true
                }
                else -> super.onOptionsItemSelected(it)
            }
        }

        return view
    }

    private fun onItemClick(reporte: Reporte, view: View) {
        Toast.makeText(context, reporte.nombre, Toast.LENGTH_SHORT).show()
        model.setReportDetail(reporte)
        view.findNavController().navigate(R.id.action_ReportListFragment_to_ReportItemFragment)
    }
}