package com.example.proyectovacio

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.navGraphViewModels
import com.example.proyectovacio.databinding.FragmentConfirmBinding

var REQUEST_IMAGE_CAPTURE = 1

class ConfirmFragment : Fragment() {

    private var _binding: FragmentConfirmBinding? = null
    private val binding get() = _binding!!

    private val model: MyViewModel by navGraphViewModels(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentConfirmBinding.inflate(layoutInflater)
        val view = binding.root

        binding.textView2.text = "${model.getNombre()} - ${model.getTipoPesca()}"
        binding.textView4.text = model.getDate()
        binding.imageView2.setImageBitmap(model.getImage())

        return view
    }
}