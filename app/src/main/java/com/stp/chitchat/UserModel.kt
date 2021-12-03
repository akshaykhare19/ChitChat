package com.stp.chitchat

data class UserModel(
    var phoneNumber: String? = "",
    var userName: String = "",
    var userImage: String = "",
    var userBio: String = "",
    val userId: String = ""
)
