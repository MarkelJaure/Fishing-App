package com.example.fishingapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.fishingapp.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_login,container,false)
        binding.lifecycleOwner = this
        val view = binding.root

        binding.loginButton.setOnClickListener {
            if (binding.textUser.text.toString() == "admin" && binding.textPassword.text.toString() == "1234") {
                startActivity(Intent(context, MainActivity::class.java))
            } else {
                Toast.makeText(context, "Usuario y/o contraseña incorrectos", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        return view
    }
}