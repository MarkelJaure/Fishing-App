package com.example.fishingapp.reglamentaciones

import android.app.Dialog
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.fishingapp.R
import com.example.fishingapp.adapters.ReglamentacionAdapter
import com.example.fishingapp.databinding.FragmentMapsBinding
import com.example.fishingapp.databinding.FragmentReglamentacionListBinding
import com.example.fishingapp.models.Reglamentacion
import com.example.fishingapp.models.Reporte
import com.example.fishingapp.reportes.MapUbicationFilter
import com.example.fishingapp.viewModels.ReglamentacionViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.util.*

class ReglamentacionListFragment : Fragment() {

    private lateinit var binding: FragmentReglamentacionListBinding
    private val reglamentacionModel: ReglamentacionViewModel by navGraphViewModels(R.id.navigation)
    private val ubicationToFilter = MapUbicationFilter2()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_reglamentacion_list,container,false)
        binding.lifecycleOwner = this
        val view = binding.root

        val reglamentacionList: RecyclerView = binding.list

        val reglamentacionAdapter = ReglamentacionAdapter()
        reglamentacionList.adapter = reglamentacionAdapter

        reglamentacionModel.allReglamentaciones.observe(viewLifecycleOwner) { reglamentaciones ->
            reglamentacionAdapter.reglamentaciones = reglamentaciones
        }

        //Observacion de la ubicacion a filtrar
        reglamentacionModel.isUbicationFilterApplied.observe(viewLifecycleOwner) { isUbicationFilterApplied ->
            reglamentacionAdapter.reglamentaciones = checkReportFilters()
            binding.toolBar.menu.findItem(R.id.QuitUbicacionFilter).isVisible = isUbicationFilterApplied
        }

        binding.toolBar.menu.findItem(R.id.DateFilter).isVisible = false
        binding.toolBar.menu.findItem(R.id.QuitDateFilter).isVisible = false

        binding.toolBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.UbicacionFilter -> {
                    ubicationToFilter.show(
                        parentFragmentManager, "UBICATION PICK")
                    true
                }
                R.id.QuitUbicacionFilter -> {
                    reglamentacionModel.setIsUbicationFilterApplied(false);
                    true
                }
                else -> super.onOptionsItemSelected(it)
            }
        }
        return view
    }

    private fun checkReportFilters(): List<Reglamentacion> {
        var reglamentacionesFIltradas: List<Reglamentacion> = reglamentacionModel.allReglamentaciones.value!!

        if (reglamentacionModel.isUbicationFilterApplied.value == true) {
            reglamentacionesFIltradas = filterByUbication(reglamentacionesFIltradas)
        }

        return reglamentacionesFIltradas
    }

    private fun filterByUbication(someReglamentaciones: List<Reglamentacion>): List<Reglamentacion> {
        return someReglamentaciones.filter { reglamentacion ->
            isMarkerInsideCircle(
                reglamentacionModel.centerPoint.value!!,
                LatLng(reglamentacion.latitud, reglamentacion.longitud),
                reglamentacion.radius
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
}

class MapUbicationFilter2 : DialogFragment(), OnMapReadyCallback {

    private val reglamentacionViewModel: ReglamentacionViewModel by navGraphViewModels(R.id.navigation)
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
        binding.ZoomInCircleButton?.isVisible= false
        binding.ZoomOutCircleButton?.isVisible= false
        binding.ApllyUbicationFilterButton!!.setOnClickListener{ ApplyUbicationFilter()}
        binding.CancelFilterButton!!.setOnClickListener{ CancelUbicationFilter()}


        val customDialog = Dialog(requireActivity())
        customDialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        customDialog.setContentView(view)
        return customDialog
    }

    var markerToFilter: Marker? = null;
    private fun ubicacionFilter(aPosition: LatLng){
        markerToFilter?.remove();
        markerToFilter = mMap.addMarker(
            MarkerOptions()
                .position(aPosition)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
    }

    private fun ApplyUbicationFilter(){
        if (markerToFilter !==null){
            reglamentacionViewModel.setCenterPoint(markerToFilter!!.position)
            reglamentacionViewModel.setIsUbicationFilterApplied(true);
            dismiss()
        } else{
            Toast.makeText(context, "Seleccione una ubiacion para filtrar", Toast.LENGTH_SHORT).show()
        }

        Log.w("Apply Filter", reglamentacionViewModel.isUbicationFilterApplied.value.toString())
    }

    private fun CancelUbicationFilter(){
        Log.w("Cancel filter", "Cancelando filtro")
        reglamentacionViewModel.setIsUbicationFilterApplied(false);
        dismiss()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.clear()
        val argentinaBounds = LatLngBounds(
            LatLng((-54.0), -75.0),  // SW bounds
            LatLng((-40.0), -50.0) // NE bounds
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(argentinaBounds, 0))

        mMap.setOnMapClickListener {
            ubicacionFilter(it)
        }

        showReglamentaciones()

    }

    private fun showReglamentaciones() {
        if(reglamentacionViewModel.allReglamentaciones.value != null) {
            for (reglamentacion in reglamentacionViewModel.allReglamentaciones.value!!) {

                mMap.addCircle(
                    CircleOptions()
                    .center(LatLng(reglamentacion.latitud, reglamentacion.longitud))
                    .radius(reglamentacion.radius)
                        .strokeColor(Color.parseColor("#D8002050"))
                        .fillColor(Color.parseColor("#9C85ABF6")))

                mMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(reglamentacion.latitud, reglamentacion.longitud))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
            }
        }
    }

}