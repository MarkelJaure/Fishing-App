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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val model: MyViewModel by navGraphViewModels(R.id.navigation)
    private val reporteModel: ReporteViewModel by navGraphViewModels(R.id.navigation)
    private val eventoModel: EventoViewModel by navGraphViewModels(R.id.navigation)
    private val reglamentacionesModel: ReglamentacionViewModel by navGraphViewModels(R.id.navigation)
    private val concursosModel: ConcursoViewModel by navGraphViewModels(R.id.navigation)

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

        reporteModel.borrarTodos()
        loadReportesFirebase()

        eventoModel.borrarTodos()
        loadEventosFirebase()

        reglamentacionesModel.borrarTodos()
        loadReglamentacionesFirebase()

        concursosModel.borrarTodos()
        loadConcursosFirebase()

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
            "EventoListFragment" ->view.findNavController().navigate(R.id.action_HomeFragment_to_eventoListFragment)
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

    private fun loadReglamentacionesFirebase() {
        FirebaseFirestore.getInstance().collection("reglamentaciones").get().addOnSuccessListener { documents ->
            for (document in documents) {
                reglamentacionesModel.insert(
                    Reglamentacion(
                        0,
                        document.id,
                        document.get("nombre") as String,
                        document.get("descripcion") as String,
                        document.get("latitud") as Double,
                        document.get("longitud") as Double,
                        (document.get("radius") as Long).toDouble(),
                        document.get("ubicacion") as String,
                    )
                )
            }
        }
    }

    private fun loadConcursosFirebase() {
        FirebaseFirestore.getInstance().collection("concursos").get().addOnSuccessListener { documents ->
            for (document in documents) {
                var idsBasesAndCondiciones = document.get("basesYCondiciones") as List<String>;
                var idsReportes = document.get("ranking") as List<String>;


                var basesAndCondiciones: List<BaseOrCondicion> = listOf()
                var ranking: List<Reporte> = listOf()

                FirebaseFirestore.getInstance().collection("basesOrCondiciones").get().addOnSuccessListener { documents ->
                    for (document in documents) {
                        if (idsBasesAndCondiciones.contains(document.id)){
                            var aBaseOrCondicion = BaseOrCondicion(
                                document.get("articulo") as String,
                                document.get("descripcion") as String
                            )
                            basesAndCondiciones = basesAndCondiciones.plus(aBaseOrCondicion)
                        }
                    }
                }.addOnCompleteListener {

                    FirebaseFirestore.getInstance().collection("reportes").get().addOnSuccessListener { documents ->
                        for (document in documents) {
                            if (idsReportes.contains(document.id)){
                                var aReporte = Reporte(
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
                                ranking = ranking.plus(aReporte)
                            }
                        }
                    }.addOnCompleteListener {

                        var aConcurso =  Concurso(
                            0,
                            document.id,
                            document.get("nombre") as String,
                            BasesAndCondiciones(basesAndCondiciones),
                            document.get("premio") as String,
                            Ranking(ranking),
                        )

                        concursosModel.insert(aConcurso)
                    }
                }
            }
        }
    }

    private fun loadEventosFirebase() {
        FirebaseFirestore.getInstance().collection("eventos").get().addOnSuccessListener { documents ->
            for (document in documents) {
                eventoModel.insert(
                    Evento(
                        0,
                        document.id,
                        document.get("nombre") as String,
                        document.get("tipoEvento") as String,
                        document.get("date") as String,
                        document.get("imagenes") as List<String>,
                        document.get("latitud") as Double,
                        document.get("longitud") as Double
                    )
                )
            }
        }
    }
}

