package com.example.fishingapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.fishingapp.databinding.FragmentLogoutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LogoutFragment : Fragment() {

    private lateinit var binding: FragmentLogoutBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_logout,container,false)
        binding.lifecycleOwner = this
        val view = binding.root

        Log.i("login", "logout")
        auth = Firebase.auth
        auth.signOut()
        startActivity(Intent(context, SplashActivity::class.java))

        return view
    }
}