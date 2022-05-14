package com.app.sehatin.data.repository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.User
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File

const val USER_COLLECTION = "User"
const val USER_IMAGE_CHILD = "USER_IMAGE"

class AuthenticationRepository() {

    private val authRef = FirebaseAuth.getInstance()
    private val userRef = FirebaseFirestore.getInstance().collection(USER_COLLECTION)

    fun login(loginState: MutableLiveData<Result<User>>, email: String, password: String) {
        loginState.value = Result.Loading
        authRef.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                loadUser(loginState, it)
            }
            .addOnFailureListener {
                it.localizedMessage?.let { msg ->
                    loginState.value = Result.Error(msg)
                }
            }
    }

    private fun loadUser(loginState: MutableLiveData<Result<User>>, it: AuthResult) {
        val authResult = it.user
        authResult?.let {
            FirebaseFirestore.getInstance().collection(USER_COLLECTION)
                .whereEqualTo(User.ID, authResult.uid)
                .get()
                .addOnSuccessListener { documents ->
                    for(document in documents) {
                        val user = document.toObject(User::class.java)
                        User.currentUser = user
                        loginState.value = Result.Success(user)
                        Log.d(TAG, "loadUser: $user")
                        return@addOnSuccessListener
                    }
                }
                .addOnFailureListener {
                    it.localizedMessage?.let { msg ->
                        loginState.value = Result.Error(msg)
                    }
                }
        }
    }

    fun register(registerState: MutableLiveData<Result<User>>, email: String, password: String, userData: Map<String, Any?>) {
        registerState.value = Result.Loading
        authRef.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                result.user?.let { 
                    createUser(registerState, it.uid, userData)
                }
            }
            .addOnFailureListener {
                registerState.value = Result.Error(it.toString())
            }
    }

    private fun createUser(registerState: MutableLiveData<Result<User>>, userId: String, userData: Map<String, Any?>) {
        val file = userData[User.IMAGE_URL] as File?
        val user = User(
            id = userId,
            username = userData[User.USERNAME] as String,
            email = userData[User.EMAIL] as String,
            dateOfBirth = userData[User.DATE_OF_BIRTH] as String,
            imageUrl = "default",
            gender = userData[User.GENDER] as Int,
            diseases = null
        )

        if(file != null) {
            val storageRef = FirebaseStorage.getInstance().getReference("$USER_IMAGE_CHILD/$userId/${System.currentTimeMillis()}.jpg")
            val uploadTask = storageRef.putFile(Uri.fromFile(file))
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@continueWithTask storageRef.downloadUrl
            }.addOnCompleteListener {
                if(it.isSuccessful) {
                    Log.d(TAG, "addOnCompleteListener: success : ${it.result}")
                    user.imageUrl = it.result.toString()
                    saveUser(registerState, user)
                }  else {
                    Log.e(TAG, "addOnCompleteListener: failed = ${it.exception}", )
                }
            }.addOnFailureListener {
                Log.e(TAG, "addOnFailureListener: $it")
                registerState.value = Result.Error(it.toString())
            }
        } else {
            saveUser(registerState, user)
        }
    }

    private fun saveUser(registerState: MutableLiveData<Result<User>>, user: User) {
        User.currentUser = user
        user.id?.let {
            userRef.document(it).set(user)
                .addOnSuccessListener {
                    registerState.value = Result.Success(user)
                }
                .addOnFailureListener { e ->
                    e.localizedMessage?.let { msg ->
                        registerState.value = Result.Error(msg)
                    }
                }
        }
    }

    private companion object {
        const val TAG = "AuthenticationRepository"
    }

}