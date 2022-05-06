package com.example.fishingapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.fishingapp.adapters.ReglamentacionAdapter
import com.example.fishingapp.databinding.FragmentReglamentacionListBinding
import com.example.fishingapp.models.Reglamentacion
import com.example.fishingapp.viewModels.ReglamentacionViewModel

class ReglamentacionListFragment : Fragment() {

    private lateinit var binding: FragmentReglamentacionListBinding
    private val reglamentacionModel: ReglamentacionViewModel by navGraphViewModels(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_reglamentacion_list,container,false)
        binding.lifecycleOwner = this
        val view = binding.root



        val reglamentacionList: RecyclerView = binding.list

        val reglamentacionAdapter = ReglamentacionAdapter()
        reglamentacionList.adapter = reglamentacionAdapter

        reglamentacionModel.allReglamentaciones.observe(viewLifecycleOwner) { reglamentaciones ->
            Log.w("reglamentacion room", reglamentaciones.toString())
            reglamentacionAdapter.reglamentaciones = reglamentaciones
        }
        return view
    }
}