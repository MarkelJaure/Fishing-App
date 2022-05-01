package com.example.fishingapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import com.example.fishingapp.MyViewModel
import com.example.fishingapp.databinding.FragmentConcursoItemBinding

class ConcursoItemFragment: Fragment() {

    private lateinit var binding: FragmentConcursoItemBinding
    private val model: MyViewModel by navGraphViewModels(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_concurso_item,container,false)
        binding.lifecycleOwner = this
        binding.model = model
        val view = binding.root

        binding.concursoNombreDetail.text = model.getConcursoDetail()?.nombre
        binding.concursoPremioDetail.text = model.getConcursoDetail()?.premio
        binding.concursoBasesAndCondicionesDetail.text = model.getConcursoDetail()?.basesAndCondiciones

        return view
    }
}

