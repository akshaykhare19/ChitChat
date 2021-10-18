package com.stp.chitchat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.stp.chitchat.databinding.FragmentChatBinding

class Chat : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding

    val args: ChatArgs by navArgs()

    private var hisId: String? = null
    private var hisName: String? = null
    private var chatId: String? = null
    private lateinit var appUtil: AppUtil
    private lateinit var myId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false)
        appUtil = AppUtil()
        myId = appUtil.getUid()!!

        hisId = args.uniqueId
        hisName = args.userName



        return binding?.root
    }

    private fun checkChat(hisId: String) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("ChatList").child(myId)
        val query = databaseReference.orderByChild("member").equalTo(hisId)
        query.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    for(dataSnap in snapshot.children) {
                        val member = dataSnap.child("member").value.toString()
                        if(hisId == member) {
                            chatId = dataSnap.key
                            break
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }


}