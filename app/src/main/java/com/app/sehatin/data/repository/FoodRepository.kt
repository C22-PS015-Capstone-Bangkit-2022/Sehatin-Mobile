package com.app.sehatin.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.Food
import com.app.sehatin.data.remote.ApiService

class FoodRepository(private val apiService: ApiService) {

    fun getGoodFoods(token: String): LiveData<Result<List<Food>?>> = liveData {
        emit(Result.Loading)
        try {
            val returnValue = MutableLiveData<Result<List<Food>?>>()
            val response = apiService.getGoodFoods(ApiService.bearerToken(token))
            if(response.isSuccessful) {
                returnValue.value = Result.Success(response.body())
                emitSource(returnValue)
            } else {
                response.errorBody()?.string()?.let {
                    Log.e(TAG, "getFood: $it")
                    emit(Result.Error(it))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "getFood: $e")
            emit(Result.Error(e.toString()))
        }
    }

    companion object {
        private const val TAG = "FoodRepository"
    }
}