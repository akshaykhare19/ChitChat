package com.stp.chitchat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.stp.chitchat.databinding.FragmentOtherUserProfileBinding

class OtherUserProfile : Fragment() {

    private var _binding: FragmentOtherUserProfileBinding? = null
    private val binding get() = _binding

    private var userId: String? = null
    private lateinit var appUtil: AppUtil

    val args: OtherUserProfileArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOtherUserProfileBinding.inflate(inflater, container, false)

        appUtil = AppUtil()

        getUserData(args.userId)

        return binding?.root
    }

    private fun getUserData(userId: String?) {
        val databaseReference = FirebaseDatabase.getInstance()
                .getReference("Users").child(userId!!)

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    val userModel = snapshot.getValue(UserModel::class.java)
                    binding!!.userDataModel = userModel
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

}