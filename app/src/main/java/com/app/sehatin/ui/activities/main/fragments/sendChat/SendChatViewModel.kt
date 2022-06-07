package com.app.sehatin.ui.activities.main.fragments.sendChat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.Chat
import com.app.sehatin.data.model.Doctor
import com.app.sehatin.data.model.User
import com.app.sehatin.data.repository.ChatRepository
import com.app.sehatin.data.repository.DoctorRepository
import com.app.sehatin.data.repository.UserRepository

class SendChatViewModel(
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository,
    private val doctorRepository: DoctorRepository
) : ViewModel(){

    val getChatState = MutableLiveData<Result<List<Chat>>>()
    val getUserState = MutableLiveData<Result<User?>>()
    val getDoctorState = MutableLiveData<Result<Doctor>>()

    fun getChat(userId: String, withUserId: String) = chatRepository.getChat(getChatState, userId, withUserId)

    fun sendChat(userId: String, withUserId: String, message: String, isDoctor: Boolean? = null) = chatRepository.sendChat(userId, withUserId, message, isDoctor)

    fun getUserData(userId: String) = userRepository.getUserData(getUserState, userId)
    fun getDoctorData(doctorId: String) = doctorRepository.getDoctor(getDoctorState, doctorId)

}