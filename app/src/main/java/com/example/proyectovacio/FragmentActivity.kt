package com.example.proyectovacio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.proyectovacio.databinding.ActivityFragmentBinding

class FragmentActivity : AppCompatActivity(), Coordinadora {

    private lateinit var binding: ActivityFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFragmentBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onCambioDeModo(index: Int) {
        val fragment = supportFragmentManager.findFragmentById(R.id.descripcionModosDePesca) as DescripcionFragment
        fragment.cambiarModoPesca(index)
    }
}