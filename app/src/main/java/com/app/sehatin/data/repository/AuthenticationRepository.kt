package com.app.sehatin.data.repository

import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataScope
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.app.sehatin.data.remote.ApiService
import com.app.sehatin.data.remote.response.LoginResponse
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File

const val USER_COLLECTION = "User"
const val USER_IMAGE_CHILD = "USER_IMAGE"

class AuthenticationRepository(private val apiService: ApiService) {

    private val authRef = FirebaseAuth.getInstance()
    private val userRef = FirebaseFirestore.getInstance().collection(USER_COLLECTION)
    private val storageRef = FirebaseStorage.getInstance().reference.child(USER_IMAGE_CHILD)

    fun login(email: String, password: String): LiveData<Result<LoginResponse?>> = liveData {
        emit(Result.Loading)
        try {
            val returnValue = MutableLiveData<Result<LoginResponse?>>()
            val response = apiService.login(email, password)
            if(response.isSuccessful) {
                returnValue.value = Result.Success(response.body())
                emitSource(returnValue)
            } else {
                Log.e(TAG, "login failed: ${response.errorBody()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "login error: ${e.localizedMessage}")
            emit(Result.Error(e.toString()))
        }
    }

    fun register(
        registerState: MutableLiveData<Result<User>>,
        email: String,
        password: String,
        userData: Map<String, Any?>) {
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
        val file = userData["image"] as File
        val uploadTask = storageRef.child(userId).putFile(file.toUri())
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            storageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                val user = User(
                    id = userId,
                    username = userData[User.USERNAME] as String,
                    email = userData[User.EMAIL] as String,
                    dateOfBirth = userData[User.DATE_OF_BIRTH] as String,
                    imageUrl = downloadUri.toString(),
                    gender = userData[User.GENDER] as Int,
                    diseases = null
                )
                userRef.document(userId).set(user).addOnCompleteListener {
                    registerState.value = Result.Success(user)
                }
            } else {
               registerState.value = Result.Error(task.exception.toString())
            }
        }
    }

    private companion object {
        const val TAG = "AuthenticationRepository"
    }

}