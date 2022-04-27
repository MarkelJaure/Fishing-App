package com.example.proyectovacio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectovacio.databinding.FragmentDescripcionBinding
import com.example.proyectovacio.databinding.FragmentReglamentacionListBinding

class ReglamentacionListFragment : Fragment() {

    private lateinit var binding: FragmentReglamentacionListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_reglamentacion_list,container,false)
        binding.lifecycleOwner = this
        val view = binding.root

        val reglamentacionList: RecyclerView = binding.list // (1)

        val articleAdapter = ReporteAdapter { reporte -> onItemClick(reporte) } // (2)
        reglamentacionList.adapter = articleAdapter // (3)

        articleAdapter.reportes = Report.data // (4)
        return view
    }

    private fun onItemClick(reporte: Report.Reporte) {
        Toast.makeText(context, reporte.nombre, Toast.LENGTH_SHORT).show()
    }
}