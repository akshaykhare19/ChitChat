package com.stp.chitchat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

        binding!!.hisName.text = hisName

        binding!!.sendBtn.setOnClickListener {
            val message = binding!!.typeBox.text.toString()
            if(message.isEmpty())
                Toast.makeText(requireContext(), "Write something...", Toast.LENGTH_SHORT).show()
            else sendMessage(message)
        }

        if(chatId != null) checkChat(hisId!!)


        return binding?.root
    }

    private fun checkChat(hisId: String) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("ChatList").child(myId)
        val query = databaseReference.orderByChild("member").equalTo(hisId)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (dataSnap in snapshot.children) {
                        val member = dataSnap.child("member").value.toString()
                        if (hisId == member) {
                            chatId = dataSnap.key
                            break
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun createChat(message: String) {
        var databaseReference = FirebaseDatabase.getInstance().getReference("ChatList").child(myId)
        chatId = databaseReference.push().key
        val myChatModel = ChatModel(chatId!!, message, System.currentTimeMillis().toString(), hisId!!)
        databaseReference.child(chatId!!).setValue(myChatModel)

        databaseReference = FirebaseDatabase.getInstance().getReference("ChatList").child(hisId!!)
        val hisChatModel = ChatModel(chatId!!, message, System.currentTimeMillis().toString(), myId)
        databaseReference.child(chatId!!).setValue(hisChatModel)

        databaseReference = FirebaseDatabase.getInstance().getReference("Chat").child(chatId!!)
        val messageModel = MessageModel(myId, hisId!!, message, type = "text")
        databaseReference.push().setValue(messageModel)


    }

    private fun sendMessage(message: String) {
        if(chatId == null) createChat(message)
        else {
            var databaseReference = FirebaseDatabase.getInstance().getReference("Chat").child(chatId!!)
            val messageModel = MessageModel(myId, hisId!!, message, type = "text")
            databaseReference.push().setValue(messageModel)

            val map: MutableMap <String, Any> = HashMap()
            map["lastMessage"] = message
            map["date"] = System.currentTimeMillis().toString()
            databaseReference = FirebaseDatabase.getInstance().getReference("ChatList").child(myId).child(chatId!!)
            databaseReference.updateChildren(map)

            databaseReference = FirebaseDatabase.getInstance().getReference("ChatList").child(hisId!!).child(chatId!!)
            databaseReference.updateChildren(map)
        }
    }
}