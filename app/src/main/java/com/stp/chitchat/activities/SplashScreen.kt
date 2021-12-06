package com.stp.chitchat.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import com.stp.chitchat.AppUtil
import com.stp.chitchat.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var appUtil: AppUtil
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        appUtil = AppUtil()

        Handler().postDelayed({

            if(auth!!.currentUser == null) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            else {
                FirebaseMessaging.getInstance().token
                    .addOnCompleteListener(OnCompleteListener {
                        if(it.isSuccessful) {
                            val token = it.result
                            val databaseReference = FirebaseDatabase
                                .getInstance()
                                .getReference("Users")
                                .child(appUtil.getUid()!!)

                            val map: MutableMap<String, Any> = HashMap()
                            map["token"] = token!!
                            databaseReference.updateChildren(map)
                        }
                    })
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }, 2000L)

    }
}