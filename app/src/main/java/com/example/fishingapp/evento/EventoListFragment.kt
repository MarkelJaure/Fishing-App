package com.example.fishingapp.evento

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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.fishingapp.R
import com.example.fishingapp.adapters.EventoAdapter
import com.example.fishingapp.databinding.FragmentEventoListBinding
import com.example.fishingapp.databinding.FragmentReportListBinding
import com.example.fishingapp.models.Evento
import com.example.fishingapp.viewModels.EventoViewModel
import com.example.fishingapp.viewModels.MyViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.max
import kotlin.math.min


class EventoListFragment : Fragment() {

    private lateinit var binding: FragmentEventoListBinding
    private val model: MyViewModel by navGraphViewModels(R.id.navigation)
    private val eventoModel: EventoViewModel by navGraphViewModels(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_evento_list, container, false)
        binding.lifecycleOwner = this
        binding.model = model

        val view = binding.root

        eventoModel.clearCloudEventos()
        loadEventosFirebase()

        model.setNombreEvento("")
        model.setTipoEvento("")
        model.setDateEvento("")
        model.setCoordenadasEvento(null)
        model.setImagesEvento(listOf())

        val eventoList: RecyclerView = binding.list

        val eventoAdapter = EventoAdapter { evento -> onItemClick(evento, view) }
        eventoList.adapter = eventoAdapter

        eventoModel.allEventos.observe(viewLifecycleOwner) { eventos ->
            Log.w("eventos room", eventos.toString())
            eventoAdapter.eventos = eventos
        }

        binding.fab.setOnClickListener {
            model.setEditEvento(false)
            model.setEventoDetail(null)
            view.findNavController().navigate(R.id.action_eventoListFragment_to_eventoFormFragment)
        }

        return view
    }

    private fun loadEventosFirebase() {
        FirebaseFirestore.getInstance().collection("eventos").get().addOnSuccessListener { documents ->
            for (document in documents) {
                eventoModel.load(
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

    private fun onItemClick(evento: Evento, view: View) {
        Toast.makeText(context, evento.nombre, Toast.LENGTH_SHORT).show()
        model.setEventoDetail(evento)
        view.findNavController().navigate(R.id.action_eventoListFragment_to_eventoItemFragment)
    }
}