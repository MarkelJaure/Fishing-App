package com.example.proyectovacio

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.proyectovacio.databinding.FragmentConfirmBinding

var REQUEST_IMAGE_CAPTURE = 1
class ConfirmFragment : Fragment() {

    private var _binding: FragmentConfirmBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentConfirmBinding.inflate(layoutInflater)
        val view = binding.root

        //TODO: CAMBIAR A VIEWBINDING
        //val message = intent.getStringExtra(EXTRA_MESSAGE)
        //val date = intent.getStringExtra(finalDate)

        // Capture the layout's TextView and set the string as its text
        //binding.textView2.apply {text = message}

        //binding.textView4.apply {text = date}

        binding.button.setOnClickListener{ dispatchTakePictureIntent()}
        return view
    }

    private fun dispatchTakePictureIntent() {

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            activity?.let {
                takePictureIntent.resolveActivity(it.packageManager).also {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.imageView.setImageBitmap(imageBitmap)
        }
    }
}