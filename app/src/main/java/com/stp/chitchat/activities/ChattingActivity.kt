package com.stp.chitchat.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.navArgs
import com.stp.chitchat.Chat
import com.stp.chitchat.OtherUserInfoReceiver
import com.stp.chitchat.databinding.ActivityChattingBinding

class ChattingActivity : AppCompatActivity(), OtherUserInfoReceiver {

    private lateinit var binding: ActivityChattingBinding
    val args: ChattingActivityArgs by navArgs()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChattingBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun getId(): String {
        return args.uniqueUserId
    }

    override fun getName(): String {
        return args.userName
    }
}