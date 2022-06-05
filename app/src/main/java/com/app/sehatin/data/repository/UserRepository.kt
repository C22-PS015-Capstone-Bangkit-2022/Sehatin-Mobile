package com.app.sehatin.data.repository

import androidx.lifecycle.MutableLiveData
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.DoctorActiveSession
import com.app.sehatin.data.model.User
import com.app.sehatin.injection.Injection

class UserRepository {
    private val userRef = Injection.provideUserCollection()
    private val searchUserLimit = 10L

    fun searsUser(keyword: String, searchUserState: MutableLiveData<Result<List<User>>>) {
        searchUserState.value = Result.Loading
        userRef
            .orderBy(User.USERNAME)
            .startAt(keyword)
            .endAt(keyword + "\uf8ff")
            .limit(searchUserLimit)
            .get()
            .addOnSuccessListener { docs ->
                val users = mutableListOf<User>()
                for(doc in docs) {
                    val user = doc.toObject(User::class.java)
                    users.add(user)
                }
                searchUserState.value = Result.Success(users)
            }
            .addOnFailureListener {
                it.localizedMessage?.let { msg ->
                    searchUserState.value = Result.Error(msg)
                }
            }
    }

    fun getUserData(getUserState: MutableLiveData<Result<User?>>, userId: String) {
        getUserState.value = Result.Loading
        userRef
            .document(userId)
            .get()
            .addOnSuccessListener {
                val user = it.toObject(User::class.java)
                getUserState.value = Result.Success(user)
            }
            .addOnFailureListener {
                getUserState.value = Result.Error(it.toString())
            }
    }

}