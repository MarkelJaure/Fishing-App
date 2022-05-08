package com.example.fishingapp.reportes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import com.example.fishingapp.R
import com.example.fishingapp.databinding.FragmentConfirmBinding
import com.example.fishingapp.databinding.FragmentFormBinding
import com.example.fishingapp.models.Reporte
import com.example.fishingapp.viewModels.MyViewModel
import com.example.fishingapp.viewModels.ReporteViewModel
import java.text.SimpleDateFormat
import java.util.*

var REQUEST_IMAGE_CAPTURE = 1

class ConfirmFragment : Fragment() {

    private lateinit var binding: FragmentConfirmBinding

    private val model: MyViewModel by navGraphViewModels(R.id.navigation)
    private val reporteModel: ReporteViewModel by navGraphViewModels(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //_binding = FragmentConfirmBinding.inflate(layoutInflater)
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_confirm,container,false)
        binding.lifecycleOwner = this
        val view = binding.root

        binding.textView2.text = "${model.getNombre()} - ${model.getTipoPesca()}"
        binding.textView4.text = model.date.value
        binding.imageView2.setImageBitmap(model.image.value)

        binding.insertButton.setOnClickListener{ saveReporte(view)}

        /*reporteModel.insert(Report.Reporte(1,model.getNombre(), model.getTipoPesca(),
            model.date.value.toString(), model.image.value))
*/
        return view
    }

    fun saveReporte(view: View){
        var newReporte = Reporte(model.getNombre(),model.getTipoPesca(),model.date.value.toString(),R.drawable.pesca)
        reporteModel.insert(newReporte)
        clearReportOnViewModel()
        view.findNavController().navigate(R.id.action_formConfirmFragment_to_reportListFragment)
    }

    fun clearReportOnViewModel(){
        model.setNombre("")
        model.setTipoPesca("")
        model.setImage(null)
        val selectedDate = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(Calendar.getInstance().time)
        model.setDate(selectedDate.toString())
    }
}