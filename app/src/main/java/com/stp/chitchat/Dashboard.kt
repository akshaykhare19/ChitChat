package com.stp.chitchat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.stp.chitchat.databinding.FragmentDashboardBinding

class Dashboard : Fragment() {

    private lateinit var _binding: FragmentDashboardBinding
    private val binding get() = _binding

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        if(auth.currentUser == null) {
            val action = DashboardDirections.actionDashboardToLogin()
            findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.signOutBtn.setOnClickListener {
            auth.signOut()
            val action = DashboardDirections.actionDashboardToLogin()
            findNavController().navigate(action)
        }
    }
}