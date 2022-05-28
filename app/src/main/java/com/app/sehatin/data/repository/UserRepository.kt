package com.app.sehatin.data.repository

import androidx.lifecycle.MutableLiveData
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.User
import com.app.sehatin.injection.Injection

class UserRepository {
    private val userRef = Injection.provideUserCollection()

    fun searsUser(keyword: String, searchUserState: MutableLiveData<Result<List<User>>>) {
        searchUserState.value = Result.Loading
        userRef
            .orderBy(User.USERNAME)
            .startAt(keyword)
            .endAt(keyword + "\uf8ff")
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

}