package com.example.fishingapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.fishingapp.adapters.ReglamentacionAdapter
import com.example.fishingapp.databinding.FragmentReglamentacionListBinding
import com.example.fishingapp.models.Reglamentacion

class ReglamentacionListFragment : Fragment() {

    private lateinit var binding: FragmentReglamentacionListBinding

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
        reglamentacionAdapter.reglamentaciones = Reglamentacion.data

        return view
    }
}