package com.stp.chitchat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChatViewModel: ViewModel() {

    private val _otherUserId = MutableLiveData("")
    val otherUserId: LiveData<String> = _otherUserId

    private val _otherUserName = MutableLiveData("")
    val otherUserName: LiveData<String> = _otherUserName

    fun setHisId(hisId: String) {
        _otherUserId.value = hisId
    }

    fun setHisName(hisName: String) {
        _otherUserName.value = hisName
    }

}