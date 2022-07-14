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
import com.google.firebase.auth.ktx.auth
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val model: MyViewModel by navGraphViewModels(R.id.navigation)
    private val reporteModel: ReporteViewModel by navGraphViewModels(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home,container,false)
        binding.lifecycleOwner = this
        binding.model = model

        val view = binding.root

        model.setEditReport(false)

        val itemList: RecyclerView = binding.list // (1)

        val itemAdapter = ItemAdapter { item -> onItemClick(item, view) } // (2)

        itemList.adapter = itemAdapter // (3)

        itemAdapter.items = HomeItem.data // (4)

        var myReportes = reporteModel.allReportes.value?.filter { reporte -> reporte.userID == Firebase.auth.currentUser?.uid }
        var bySpecie = myReportes?.groupBy { it.tipoEspecie }?.values?.sortedByDescending { it.size }

        val creationTimestamp = Firebase.auth.currentUser?.metadata?.creationTimestamp
        val signUpDate = SimpleDateFormat("dd/MM/yyyy").format(creationTimestamp)

        if (myReportes != null && bySpecie != null) {
            var firstText = "Pescador en actividad desde ${signUpDate}: se han cargado ${myReportes.size} reportes."
            var secondText = ""
            var thirdText = ""
            if(bySpecie.size > 0)
                secondText = " Las especies que han sido pescadas con mas frecuencia son: ${bySpecie?.get(0)?.get(0)?.tipoEspecie} (presentes en ${bySpecie?.get(0)?.size} reportes)"
            if(bySpecie.size > 1)
                thirdText = " y ${bySpecie?.get(1)?.get(0)?.tipoEspecie} (presentes en ${bySpecie?.get(1)?.size} reportes)"

            binding.initDateFisherman.text = firstText + secondText + thirdText
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


}

