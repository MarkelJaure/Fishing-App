package com.example.fishingapp.zonas

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.fishingapp.R
import com.example.fishingapp.adapters.GeoEventAdapter
import com.example.fishingapp.databinding.FragmentGeoEventListBinding
import com.example.fishingapp.models.GeoEvent
import com.example.fishingapp.models.Zona
import com.example.fishingapp.viewModels.GeoEventViewModel
import com.example.fishingapp.viewModels.MyViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class GeoEventListFragment : Fragment() {

    private lateinit var binding: FragmentGeoEventListBinding
    private val model: MyViewModel by navGraphViewModels(R.id.navigation)
    private val geoEventModel: GeoEventViewModel by navGraphViewModels(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_geo_event_list,container,false)
        binding.lifecycleOwner = this
        binding.model = model

        val view = binding.root

        val geoEventsList: RecyclerView = binding.list

        val geoEventAdapter = GeoEventAdapter { geoEvent -> onItemClick(geoEvent, view) }
        geoEventsList.adapter = geoEventAdapter

        geoEventModel.allGeoEvents.observe(viewLifecycleOwner) { geoEvents ->
            Log.w("eventos room", geoEvents.toString())
            geoEventAdapter.geoEvents = geoEvents
        }

        return view
    }

    private fun onItemClick(zona: GeoEvent, view: View) {
        Toast.makeText(context, zona.nombre, Toast.LENGTH_SHORT).show()
        //model.setEventoDetail(evento)
        //view.findNavController().navigate(R.id.action_eventoListFragment_to_eventoItemFragment)
    }
}