package com.example.fishingapp.zonas

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.navGraphViewModels
import com.example.fishingapp.R
import com.example.fishingapp.databinding.FragmentZonaListBinding
import com.example.fishingapp.models.Zona
import com.example.fishingapp.viewModels.MyViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class ZonaListFragment : Fragment() {

    private lateinit var binding: FragmentZonaListBinding
    private val model: MyViewModel by navGraphViewModels(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_zona_list,container,false)
        binding.lifecycleOwner = this
        binding.model = model

        val view = binding.root

        var zonas : List<Zona> = listOf()

        FirebaseFirestore.getInstance().collection("geofenceEvents").whereEqualTo("userID", Firebase.auth.currentUser?.uid).get().addOnSuccessListener { documents ->
            for (document in documents) {
                FirebaseFirestore.getInstance().collection("zonas")
                    .document(document.data.get("zonaID").toString()).get()
                    .addOnSuccessListener { data ->
                        zonas = zonas.plus(Zona(data.get("nombre") as String, data.get("descripcion") as String,
                            data.get("latitud") as Double, data.get("longitud") as Double, (data.get("radius") as Long).toDouble()
                        ))
                        //Log.w("notif", zonas.toString())
                        //Log.w("notif size", zonas.size.toString())

                    }.addOnCompleteListener {
                        Log.w("notif", zonas.toString())
                    }
            }
        }

        return view
    }
}