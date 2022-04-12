package com.example.proyectovacio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.proyectovacio.databinding.FragmentSelectorBinding


class SelectorFragment : Fragment() {

    private var _binding: FragmentSelectorBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSelectorBinding.inflate(inflater, container, false)
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
            else -> 0
        }
        val activity = getActivity()
        if (activity is Coordinadora) {
            activity.onCambioDeModo(index)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}