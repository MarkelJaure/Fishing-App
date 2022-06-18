package com.example.fishingapp.reportes

import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.fishingapp.R
import com.example.fishingapp.adapters.ReporteAdapter
import com.example.fishingapp.databinding.FragmentMapsBinding
import com.example.fishingapp.databinding.FragmentReportListBinding
import com.example.fishingapp.models.Reporte
import com.example.fishingapp.viewModels.MyViewModel
import com.example.fishingapp.viewModels.ReporteViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.max
import kotlin.math.min


class ReportListFragment : Fragment() {

    private lateinit var binding: FragmentReportListBinding
    private val model: MyViewModel by navGraphViewModels(R.id.navigation)
    private val reporteModel: ReporteViewModel by navGraphViewModels(R.id.navigation)
    private val ubicationToFilter = MapUbicationFilter()
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
            reporteAdapter.reportes = checkReportFilters()
            binding.toolBar.menu.findItem(R.id.QuitDateFilter).isVisible = isDateFilerApllied
        }

        //Observacion de la ubicacion a filtrar
        reporteModel.isUbicationFilterApplied.observe(viewLifecycleOwner) { isUbicationFilterApplied ->
            reporteAdapter.reportes = checkReportFilters()
            binding.toolBar.menu.findItem(R.id.QuitUbicacionFilter).isVisible = isUbicationFilterApplied
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
                R.id.UbicacionFilter -> {
                    ubicationToFilter.show(
                        parentFragmentManager, "UBICATION PICK")
                    true
                }
                R.id.QuitUbicacionFilter -> {
                    reporteModel.setIsUbicationFilterApplied(false);
                    true
                }
                else -> super.onOptionsItemSelected(it)
            }
        }

        binding.mapViewButton.setOnClickListener{ seeOnMap(view)}
        return view
    }

    private fun checkReportFilters(): List<Reporte> {
        var reportesFiltrados: List<Reporte> = reporteModel.allReportes.value!!

        if (reporteModel.isDateFilterApplied.value == true) {
            reportesFiltrados = filterByDate(reportesFiltrados)
        }

        if (reporteModel.isUbicationFilterApplied.value == true) {
            Log.w("Ub Filter with Radius: ", reporteModel.radius.value.toString())
            reportesFiltrados = filterByUbication(reportesFiltrados)
        }

        return reportesFiltrados
    }
    private fun filterByDate(someReportes: List<Reporte>): List<Reporte> {
        return someReportes.filter { reporte ->
            val dateMilis = SimpleDateFormat("dd/MM/yyyy").parse(reporte.date).time
            dateMilis >= reporteModel.initDate.value!! && dateMilis <= reporteModel.finishDate.value!!
        }
    }

    private fun filterByUbication(someReportes: List<Reporte>): List<Reporte> {
        return someReportes.filter { reporte ->
            isMarkerInsideCircle(
                reporteModel.centerPoint.value!!,
                LatLng(reporte.latitud, reporte.longitud),
                reporteModel.radius.value!!
            )
        }
    }

    private fun isMarkerInsideCircle(
        centerLatLng: LatLng,
        draggedLatLng: LatLng,
        radius: Double
    ): Boolean {
        val distances = FloatArray(1)
        Location.distanceBetween(
            centerLatLng.latitude,
            centerLatLng.longitude,
            draggedLatLng.latitude,
            draggedLatLng.longitude, distances
        )
        Log.w("Distancia entre puntos", distances[0].toString())
        return radius >= distances[0]
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

class MapUbicationFilter : DialogFragment(), OnMapReadyCallback {

    private val reporteModel: ReporteViewModel by navGraphViewModels(R.id.navigation)
    private lateinit var mMap: GoogleMap
    private lateinit var binding: FragmentMapsBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_maps,null,false)
        binding.lifecycleOwner = this
        val view = binding.root

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        binding.mapToolBar.isVisible = false
        binding.ZoomInCircleButton!!.setOnClickListener{ ZoomInUbicationCircle()}
        binding.ZoomOutCircleButton!!.setOnClickListener{ ZoomOutUbicationCircle()}
        binding.ApllyUbicationFilterButton!!.setOnClickListener{ ApplyUbicationFilter()}
        binding.CancelFilterButton!!.setOnClickListener{ CancelUbicationFilter()}


        val customDialog = Dialog(requireActivity())
        customDialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        customDialog.setContentView(view)
        return customDialog

    }

    var markerToFilter: Marker? = null;
    var circleToFilter: Circle? = null;
    var circleRadio = 1000.0;
    private fun ubicacionFilter(aPosition: LatLng){
        markerToFilter?.remove();
        markerToFilter = mMap.addMarker(
            MarkerOptions()
                .position(aPosition)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
        circleToFilter?.remove();
        circleToFilter = mMap.addCircle(
            CircleOptions()
                .center(aPosition)
                .radius(circleRadio)
                .strokeColor(Color.parseColor("#D8005011"))
                .fillColor(Color.parseColor("#9C85F685"))
        )
    }

    private fun ZoomInUbicationCircle(){
        if (circleToFilter !== null){
            circleToFilter!!.radius= min(circleToFilter!!.radius + 100.0,10000.0);
            circleRadio = circleToFilter!!.radius
            Log.w("Radius", circleToFilter!!.radius.toString())
        }
    }

    private fun ZoomOutUbicationCircle(){
        if (circleToFilter !==null){
            circleToFilter!!.radius= max(circleToFilter!!.radius - 100.0,100.0);
            circleRadio = circleToFilter!!.radius
            Log.w("Radius", circleToFilter!!.radius.toString())
        }
    }

    private fun ApplyUbicationFilter(){
        if (circleToFilter !==null){
            reporteModel.setRadius(circleToFilter!!.radius)
            reporteModel.setCenterPoint(markerToFilter!!.position)
            reporteModel.setIsUbicationFilterApplied(true);
            dismiss()
        } else{
            Toast.makeText(context, "Seleccione un area para filtrar", Toast.LENGTH_SHORT).show()
        }

        Log.w("Apply Filter", reporteModel.isUbicationFilterApplied.value.toString())
    }

    private fun CancelUbicationFilter(){
        Log.w("Cancel filter", "Cancelando filtro")
        reporteModel.setIsUbicationFilterApplied(false);
        dismiss()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.clear()

        mMap.setOnMapClickListener {
            ubicacionFilter(it)
        }

    }

}
