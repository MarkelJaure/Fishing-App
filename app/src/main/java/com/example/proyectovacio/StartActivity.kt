package com.example.proyectovacio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.proyectovacio.databinding.ActivityMainBinding
import com.example.proyectovacio.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStartBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.StartButton.setOnClickListener{ goToMainActivity()}
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java).apply{}
        startActivity(intent)
    }
}