package com.example.fishingapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.fishingapp.databinding.FragmentSelectorBinding

class SelectorFragment : Fragment() {

    private lateinit var binding: FragmentSelectorBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_selector,container,false)
        binding.lifecycleOwner = this
        val view = binding.root

        binding.CostaRadioButton.setOnClickListener{
            click_en_radio_button(R.id.CostaRadioButton) }

        binding.EmbarcacionRadioButton.setOnClickListener{
            click_en_radio_button(R.id.EmbarcacionRadioButton) }

        binding.LagoRadioButton.setOnClickListener{
            click_en_radio_button(R.id.LagoRadioButton) }
        return view
    }

    private fun click_en_radio_button(id_radio_button: Int) {
        val index = when (id_radio_button){
            R.id.CostaRadioButton -> 2
            R.id.LagoRadioButton -> 1
            R.id.EmbarcacionRadioButton-> 0
            else -> -1
        }
        val fragment = parentFragmentManager.findFragmentById(R.id.descripcionModosDePesca) as DescripcionFragment
        fragment.cambiarModoPesca(index)
    }

}