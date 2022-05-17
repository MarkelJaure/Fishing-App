package com.example.fishingapp.reportes

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import com.example.fishingapp.R
import com.example.fishingapp.databinding.FragmentReportItemBinding
import com.example.fishingapp.viewModels.MyViewModel
import java.io.File

class ReportItemFragment: Fragment() {

    private lateinit var binding: FragmentReportItemBinding
    private val model: MyViewModel by navGraphViewModels(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_report_item,container,false)
        binding.lifecycleOwner = this
        binding.model = model
        val view = binding.root

        binding.reporteNombreDetail.text = model.getReportDetail()?.nombre
        binding.reporteTipoPescaDetail.text = model.getReportDetail()?.tipoPesca
        binding.reporteFechaDetail.text = model.getReportDetail()?.date
        var imgFile = File(model.getReportDetail()?.image)
        if(imgFile.exists()) {
            binding.reporteImageDetail.setImageBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()))
        }

        binding.editButton?.setOnClickListener {
            model.setEditReport(true)
            view.findNavController().navigate(R.id.formFragment)
        }

        return view
    }
}

