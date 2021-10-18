package com.stp.chitchat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.stp.chitchat.databinding.FragmentContactsBinding

class Contacts : Fragment() {

    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding

    private lateinit var appPermission: AppPermission

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contacts, container, false)

        appPermission = AppPermission()
        if(appPermission.isContactPermission(requireContext())) {
//            getMobileContact()
        }
        else appPermission.requestContactPermission(requireActivity())

        return binding?.root
    }

}