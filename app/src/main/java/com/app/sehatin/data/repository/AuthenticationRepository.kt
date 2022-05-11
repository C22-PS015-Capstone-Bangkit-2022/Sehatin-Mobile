package com.app.sehatin.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.app.sehatin.data.remote.ApiService
import com.app.sehatin.data.remote.response.LoginResponse
import com.app.sehatin.data.Result

class AuthenticationRepository(private val apiService: ApiService) {

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

    private companion object {
        const val TAG = "AuthenticationRepository"
    }

}