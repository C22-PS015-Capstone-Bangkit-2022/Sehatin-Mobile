package com.app.sehatin.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.User
import com.app.sehatin.data.remote.ApiService
import com.app.sehatin.data.remote.response.FoodResponse

class FoodRepository(private val apiService: ApiService) {

    fun getFood(): LiveData<Result<FoodResponse?>> = liveData {
        emit(Result.Loading)
        try {
            val returnValue = MutableLiveData<Result<FoodResponse?>>()
            User.currentUser?.let {
                it.id?.let { userId ->
                    val response = apiService.getFood(userId)
                    if(response.isSuccessful) {
                        returnValue.value = Result.Success(response.body())
                        emitSource(returnValue)
                    } else {
                        emit(Result.Error("No Data"))
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "getFood: ${e.localizedMessage}")
            emit(Result.Error(e.toString()))
        }
    }

    companion object {
        private const val TAG = "FoodRepository"
    }
}