package com.app.sehatin.data.repository

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.app.sehatin.data.remote.ApiService
import com.app.sehatin.data.remote.response.LoginResponse
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.File

const val USER_COLLECTION = "User"
const val USER_IMAGE_CHILD = "USER_IMAGE"

class AuthenticationRepository(private val apiService: ApiService) {

    private val authRef = FirebaseAuth.getInstance()
    private val userRef = FirebaseFirestore.getInstance().collection(USER_COLLECTION)
    private val storageRef = Firebase.storage.reference.child(USER_IMAGE_CHILD)

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
            val uploadTask = storageRef.child(userId).child("${System.currentTimeMillis()}.jpg").putFile(Uri.fromFile(file))
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
        userRef.document(user.id).set(user).addOnCompleteListener {
            registerState.value = Result.Success(user)
        }
    }

    private companion object {
        const val TAG = "AuthenticationRepository"
    }

}