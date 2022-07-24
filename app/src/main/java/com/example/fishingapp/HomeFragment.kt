package com.example.fishingapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.fishingapp.databinding.FragmentHomeBinding
import com.example.fishingapp.models.*
import com.example.fishingapp.viewModels.*
import com.google.firebase.ktx.Firebase
import com.example.fishingapp.viewModels.MyViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.ktx.auth
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentHomeBinding
    private val model: MyViewModel by navGraphViewModels(R.id.navigation)
    private val reporteModel: ReporteViewModel by navGraphViewModels(R.id.navigation)
    private lateinit var mMap: GoogleMap
    var lastMonth: List<Reporte>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home,container,false)
        binding.lifecycleOwner = this
        binding.model = model

        val view = binding.root

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapReportHome) as SupportMapFragment
        mapFragment.getMapAsync(this)

        model.setEditReport(false)

        val itemList: RecyclerView = binding.list // (1)

        val itemAdapter = ItemAdapter { item -> onItemClick(item, view) } // (2)

        itemList.adapter = itemAdapter // (3)

        itemAdapter.items = HomeItem.data // (4)

        var calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, calendar[Calendar.MONTH] - 1)


        reporteModel.allReportes.observe(viewLifecycleOwner) { reportes ->
            var myReportes = reportes.filter { reporte -> reporte.userID == Firebase.auth.currentUser?.uid }
            if(!myReportes.isNullOrEmpty()) {
                lastMonth = myReportes.filter { reporte ->
                    val dateMilis = SimpleDateFormat("dd/MM/yyyy").parse(reporte.date).time
                    dateMilis >= calendar.time.time
                }
            }

            var bySpecie = myReportes.groupBy { it.tipoEspecie }.values.sortedByDescending { it.size }

            val creationTimestamp = Firebase.auth.currentUser?.metadata?.creationTimestamp
            val signUpDate = SimpleDateFormat("dd/MM/yyyy").format(creationTimestamp)

            if (lastMonth != null) {
                "Reportes del último mes: ${lastMonth!!.size}".also { binding.lastMonthFisherman.text = it }
            }

            if (!myReportes.isNullOrEmpty() && bySpecie.isNotEmpty()) {
                var firstText = "Pescador en actividad desde ${signUpDate}: se han cargado ${myReportes.size} reportes."
                var secondText = ""
                var thirdText = ""
                if(bySpecie.size > 0)
                    secondText = " Las especies que han sido pescadas con mas frecuencia son: ${bySpecie[0][0].tipoEspecie} (presentes en ${bySpecie[0].size} reportes)"
                if(bySpecie.size > 1)
                    thirdText = " y ${bySpecie.get(1).get(0).tipoEspecie} (presentes en ${bySpecie[1].size} reportes)"

                (firstText + secondText + thirdText).also { binding.initDateFisherman.text = it }
            }
        }



        return view
    }

    private fun onItemClick(item: HomeItem, view: View) {
        when(item.nombre){
            "FormFragment" -> view.findNavController().navigate(R.id.action_HomeFragment_to_FormFragment)
            "ReportListFragment" -> view.findNavController().navigate(R.id.action_HomeFragment_to_ReportListFragment)
            "ReglamentacionListFragment" -> view.findNavController().navigate(R.id.action_HomeFragment_to_ReglamentacionListFragment)
            "ConcursoListFragment" -> view.findNavController().navigate(R.id.action_HomeFragment_to_ConcursoListFragment)
            "FormEventFragment" ->view.findNavController().navigate(R.id.action_HomeFragment_to_FormEventFragment)
            "FormEventFragment" ->view.findNavController().navigate(R.id.action_HomeFragment_to_FormEventFragment)
            "EventoListFragment" ->view.findNavController().navigate(R.id.action_HomeFragment_to_eventoListFragment)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.clear()

        reporteModel.allReportes.observe(viewLifecycleOwner) { reportes ->
            if(lastMonth != null) {
                for (reporte in lastMonth!!) {
                    mMap.addMarker(MarkerOptions().position(LatLng(reporte.latitud, reporte.longitud)))
                }

                var minLat: Double? = null
                var maxLat: Double? = null
                var minLng: Double? = null
                var maxLng: Double? = null

                for (reporte in lastMonth!!) {

                    if (minLat ==  null || reporte.latitud < minLat){
                        minLat = reporte.latitud
                    }
                    if (maxLat ==  null || reporte.latitud > maxLat){
                        maxLat = reporte.latitud
                    }
                    if (minLng ==  null || reporte.longitud < minLng){
                        minLng = reporte.longitud
                    }
                    if (maxLng ==  null || reporte.longitud > maxLng){
                        maxLng = reporte.longitud
                    }
                }

                if (lastMonth!!.size == 1){
                    mMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                minLat!!,
                                minLng!!
                            ), 11F
                        )
                    )
                } else{
                    val promedyBound = LatLngBounds(
                        LatLng(minLat!!, minLng!!),  // SW bounds
                        LatLng(maxLat!!, maxLng!!) // NE bounds
                    )
                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(promedyBound, 150))
                }



            }
        }

    }
}

