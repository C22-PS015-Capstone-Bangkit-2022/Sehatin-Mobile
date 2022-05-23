package com.app.sehatin.data.repository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.User
import com.app.sehatin.utils.DEFAULT
import com.app.sehatin.utils.USER_COLLECTION
import com.app.sehatin.utils.USER_IMAGE_STORAGE
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File



class AuthenticationRepository {

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
            imageUrl = DEFAULT,
            gender = userData[User.GENDER] as Int,
            diseases = null
        )

        if(file != null) {
            val storageRef = FirebaseStorage.getInstance().getReference("$USER_IMAGE_STORAGE/$userId/${System.currentTimeMillis()}.jpg")
            val uploadTask = storageRef.putFile(Uri.fromFile(file))
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@continueWithTask storageRef.downloadUrl
            }.addOnSuccessListener {
                user.imageUrl = it.toString()
                saveUser(registerState, user)
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

    fun updateUser(updateUserState: MutableLiveData<Result<String>>, userId: String, image: File?, userData: MutableMap<String, Any>) {
        updateUserState.value = Result.Loading
        if(image != null) {
            val storageRef = FirebaseStorage.getInstance().getReference("$USER_IMAGE_STORAGE/$userId/${System.currentTimeMillis()}.jpg")
            val uploadTask = storageRef.putFile(Uri.fromFile(image))
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@continueWithTask storageRef.downloadUrl
            }.addOnSuccessListener {
                userData[User.IMAGE_URL] = it.toString()
                updateData(updateUserState, userId, userData)
            }.addOnFailureListener {
                Log.e(TAG, "updateUser: $it")
                updateUserState.value = Result.Error(it.toString())
            }
        } else {
            updateData(updateUserState, userId, userData)
        }
    }

    private fun updateData(updateUserState: MutableLiveData<Result<String>>, userId: String, user: Map<String, Any>) {
        userRef
            .document(userId)
            .update(user)
            .addOnSuccessListener {
                updateUserState.value = Result.Success(userId)
            }
            .addOnFailureListener {
                it.localizedMessage?.let { msg ->
                    updateUserState.value = Result.Error(msg)
                }
            }
    }

    private companion object {
        const val TAG = "AuthenticationRepository"
    }

}