package com.stp.chitchat.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.navArgs
import com.stp.chitchat.Chat
import com.stp.chitchat.databinding.ActivityChattingBinding

class ChattingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChattingBinding
    val args: ChattingActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChattingBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val intent = intent
//        val hisId = intent.getStringExtra("hisId")
//        val hisName = intent.getStringExtra("hisName")

        val hisId = args.uniqueUserId
        val hisName = args.userName

        val mBundle = Bundle()
        mBundle.putString("hisId", hisId)
        mBundle.putString("hisName", hisName)
        val mFragment = Chat()
        mFragment.arguments = mBundle



    }
}