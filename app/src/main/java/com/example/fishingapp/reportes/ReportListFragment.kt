package com.example.fishingapp.reportes

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.util.Pair
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
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
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*


class ReportListFragment : Fragment() {

    private lateinit var binding: FragmentReportListBinding
    private val model: MyViewModel by navGraphViewModels(R.id.navigation)
    private val reporteModel: ReporteViewModel by navGraphViewModels(R.id.navigation)
    private val datePicker = MaterialDatePicker.Builder.dateRangePicker().build()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_report_list,container,false)
        binding.lifecycleOwner = this
        binding.model = model

        val view = binding.root

        model.setNombre("")
        model.setTipoPesca("")
        model.setDate("")
        model.setCoordenadasReporte(null)
        model.setImage(null)

        val reporteList: RecyclerView = binding.list

        val reporteAdapter = ReporteAdapter { reporte -> onItemClick(reporte, view) }
        reporteList.adapter = reporteAdapter

        reporteModel.allReportes.observe(viewLifecycleOwner) { reportes ->
            Log.w("reportes room", reportes.toString())
            reporteAdapter.reportes = reportes
        }

        //Observacion de la fecha a filtrar
        reporteModel.isDateFilterApplied.observe(viewLifecycleOwner) { isDateFilerApllied ->
            if (isDateFilerApllied){
                reporteAdapter.reportes = reporteModel.allReportes.value!!.filter { reporte ->
                    val dateMilis = SimpleDateFormat("dd/MM/yyyy").parse(reporte.date).time
                    dateMilis >= reporteModel.initDate.value!! && dateMilis <= reporteModel.finishDate.value!!
                }
            }else{
                reporteAdapter.reportes = reporteModel.allReportes.value!!
            }

            binding.toolBar.menu.findItem(R.id.QuitDateFilter).isVisible = isDateFilerApllied
        }


        binding.fab.setOnClickListener {
            model.setEditReport(false)
            model.setReportDetail(null)
            view.findNavController().navigate(R.id.formFragment)
        }

        binding.toolBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.DateFilter -> {
                    datePicker.show(parentFragmentManager, "DATE PICK")
                    datePicker.addOnPositiveButtonClickListener { selection ->
                        reporteModel.setInitDate(selection.first)
                        reporteModel.setFinishDate(selection.second)
                        reporteModel.setIsDateFilterApplied(true)
                    }
                    true
                }
                R.id.QuitDateFilter -> {
                    reporteModel.setInitDate(null)
                    reporteModel.setFinishDate(null)
                    reporteModel.setIsDateFilterApplied(false)
                    true
                }
                else -> super.onOptionsItemSelected(it)
            }
        }

        binding.mapViewButton.setOnClickListener{ seeOnMap(view)}
        return view
    }

    private fun seeOnMap(view:View){
        model.setFilterReport(true)
        view.findNavController().navigate(R.id.action_ReportListFragment_to_MapsFragment)
    }

    private fun onItemClick(reporte: Reporte, view: View) {
        Toast.makeText(context, reporte.nombre, Toast.LENGTH_SHORT).show()
        model.setReportDetail(reporte)
        view.findNavController().navigate(R.id.action_ReportListFragment_to_ReportItemFragment)
    }
}
