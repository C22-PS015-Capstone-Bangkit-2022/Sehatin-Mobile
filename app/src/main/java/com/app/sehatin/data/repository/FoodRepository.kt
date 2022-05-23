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
            // TODO: GET FOOD
        } catch (e: Exception) {
            Log.e(TAG, "getFood: ${e.localizedMessage}")
            emit(Result.Error(e.toString()))
        }
    }

    companion object {
        private const val TAG = "FoodRepository"
    }
}