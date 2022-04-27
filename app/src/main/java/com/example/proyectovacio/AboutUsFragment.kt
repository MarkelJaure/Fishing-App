package com.example.proyectovacio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.proyectovacio.databinding.FragmentAboutUsBinding
import com.example.proyectovacio.databinding.FragmentConfirmBinding
import com.example.proyectovacio.databinding.FragmentDescripcionBinding


class AboutUsFragment : Fragment() {

    private lateinit var binding: FragmentAboutUsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_about_us,container,false)
        binding.lifecycleOwner = this

        val view = binding.root

        return view
    }


}