package com.example.fishingapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.fishingapp.databinding.FragmentHomeBinding
import com.example.fishingapp.models.Reporte
import com.example.fishingapp.models.ReporteCloud
import com.example.fishingapp.viewModels.MyViewModel
import com.example.fishingapp.viewModels.ReporteViewModel
import com.google.firebase.firestore.FirebaseFirestore


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

        reporteModel.clearCloudReportes()
        loadReportesFirebase()
        return view
    }

    private fun onItemClick(item: HomeItem, view: View) {
        when(item.nombre){
            "FormFragment" -> view.findNavController().navigate(R.id.action_HomeFragment_to_FormFragment)
            "ReportListFragment" -> view.findNavController().navigate(R.id.action_HomeFragment_to_ReportListFragment)
            "ReglamentacionListFragment" -> view.findNavController().navigate(R.id.action_HomeFragment_to_ReglamentacionListFragment)
            "ConcursoListFragment" -> view.findNavController().navigate(R.id.action_HomeFragment_to_ConcursoListFragment)
            "AboutUsFragment" -> view.findNavController().navigate(R.id.action_HomeFragment_to_AboutUsFragment)
            "FormEventFragment" ->view.findNavController().navigate(R.id.action_HomeFragment_to_FormEventFragment)
        }
        //view.findNavController().navigate(R.id.action_ConcursoListFragment_to_ConcursoItemFragment)
    }

    private fun loadReportesFirebase() {
        FirebaseFirestore.getInstance().collection("reportes").get().addOnSuccessListener { documents ->
            for (document in documents) {
                reporteModel.load(
                    Reporte(
                        0,
                        document.id,
                        document.get("nombre") as String,
                        document.get("tipoPesca") as String,
                        document.get("tipoEspecie") as String,
                        document.get("date") as String,
                        document.get("imagen") as String,
                        document.get("latitud") as Double,
                        document.get("longitud") as Double
                    )
                )
            }
        }
    }
}

