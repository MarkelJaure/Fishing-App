package com.example.fishingapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.fishingapp.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_login,container,false)
        binding.lifecycleOwner = this
        val view = binding.root
        auth = Firebase.auth

        binding.loginButton.setOnClickListener { signIn() }
        binding.registerButton?.setOnClickListener { signUp() }

        return view
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            Log.i("login", "currentUser isnt null")
            startActivity(Intent(context, MainActivity::class.java))
        }
    }

    private fun signIn() {
        auth.signInWithEmailAndPassword(binding.textUser.text.toString(), binding.textPassword.text.toString())
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d("login", "signInWithEmail:success")
                    val user = auth.currentUser
                    startActivity(Intent(context, MainActivity::class.java))
                } else {
                    Log.w("login", "signInWithEmail:failure", task.exception)
                    Toast.makeText(context, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun signUp() {
        val email = binding.textUser.text.toString()
        val password = binding.textPassword.text.toString()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d("login", "createUserWithEmail:success")
                    val user = auth.currentUser
                } else {
                    Log.w("login", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(context, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}