package com.example.proyectovacio

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import com.example.proyectovacio.databinding.ActivityDisplaymessageBinding

const val REQUEST_IMAGE_CAPTURE = 1

class DisplayMessageActivity : Activity() {

    private lateinit var binding: ActivityDisplaymessageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDisplaymessageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Get the Intent that started this activity and extract the string
        val message = intent.getStringExtra(EXTRA_MESSAGE)
        val date = intent.getStringExtra(finalDate)

        // Capture the layout's TextView and set the string as its text
        binding.textView2.apply {
            text = message
        }

        binding.textView4.apply {
            text = date
        }

        binding.button.setOnClickListener{ dispatchTakePictureIntent()}

    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager).also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.imageView.setImageBitmap(imageBitmap)
        }
    }





}