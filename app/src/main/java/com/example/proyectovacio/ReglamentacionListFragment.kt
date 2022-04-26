package com.example.proyectovacio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectovacio.databinding.FragmentReglamentacionListBinding

class ReglamentacionListFragment : Fragment() {

    private var _binding: FragmentReglamentacionListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReglamentacionListBinding.inflate(inflater, container, false)
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