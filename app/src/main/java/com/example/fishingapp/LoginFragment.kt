package com.example.fishingapp

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import com.example.fishingapp.databinding.FragmentLoginBinding
import com.example.fishingapp.viewModels.MyViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth

    var email = ""
    var password = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_login,container,false)
        binding.lifecycleOwner = this
        val view = binding.root
        auth = Firebase.auth

        var showPassword = false

        binding.loginButton.setOnClickListener { signIn() }
        binding.registerButton?.setOnClickListener { signUp() }
        binding.showPasswordButton?.setOnClickListener {
            if(showPassword) {
                showPassword = false
                binding.textPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            else {
                showPassword = true
                binding.textPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            startActivity(Intent(context, MainActivity::class.java))
        }
    }

    private fun signIn() {
        email = binding.textUser.text.toString()
        password = binding.textPassword.text.toString()
        if(email != "" && password != "") {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        startActivity(Intent(context, MainActivity::class.java))
                    } else {
                        Toast.makeText(context, task.exception.toString(),
                            Toast.LENGTH_LONG).show()
                    }
                }
        }
        else {
            Toast.makeText(context, "Complete los campos",
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun signUp() {
        email = binding.textUser.text.toString()
        password = binding.textPassword.text.toString()
        if(email != "" && password != "") {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                    } else {
                        Toast.makeText(
                            context, task.exception.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
        else {
            Toast.makeText(context, "Complete los campos",
                Toast.LENGTH_SHORT).show()
        }
    }
}