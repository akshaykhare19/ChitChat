package com.stp.chitchat

import com.google.firebase.auth.FirebaseAuth

class AppUtil {

    fun getUid(): String? {
        val firebaseAuth = FirebaseAuth.getInstance()
        return firebaseAuth.uid
    }

}