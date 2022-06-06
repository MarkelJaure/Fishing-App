package com.example.fishingapp.reportes

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.view.get
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
import java.text.SimpleDateFormat
import java.util.*


class ReportListFragment : Fragment() {

    private lateinit var binding: FragmentReportListBinding
    private val model: MyViewModel by navGraphViewModels(R.id.navigation)
    private val reporteModel: ReporteViewModel by navGraphViewModels(R.id.navigation)
    private val dateToFilter = FilterDatePicker()

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
        reporteModel.date.observe(viewLifecycleOwner) { date ->
            Log.w("Fecha filtrada", date.toString())

            if (date !== ""){
                reporteAdapter.reportes = reporteModel.allReportes.value!!.filter {
                        reporte -> reporte.date == reporteModel.date.value!!.toString()
                }
                binding.toolBar.menu.findItem(R.id.QuitDateFilter).isVisible = true
            }else{
                reporteAdapter.reportes = reporteModel.allReportes.value!!
                binding.toolBar.menu.findItem(R.id.QuitDateFilter).isVisible = false
            }

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
                    dateToFilter.show(parentFragmentManager, "DATE PICK")
                    true
                }
                R.id.QuitDateFilter -> {
                    reporteModel.setDate("")
                    reporteModel.setIsDateFilterApplied(false);
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

class FilterDatePicker : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private val calendar = Calendar.getInstance()
    private val reportesModel: ReporteViewModel by navGraphViewModels(R.id.navigation)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]
        return DatePickerDialog(requireActivity(), this, year, month, dayOfMonth)
    }

    override fun onDateSet(view: android.widget.DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val selectedDate = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(calendar.time)


        reportesModel.setDate(selectedDate.toString())
        reportesModel.setIsDateFilterApplied(true);
    }
}