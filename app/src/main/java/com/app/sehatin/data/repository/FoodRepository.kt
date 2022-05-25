package com.app.sehatin.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.app.sehatin.data.Result
import com.app.sehatin.data.remote.ApiService
import com.app.sehatin.data.remote.response.FoodResponse
import com.app.sehatin.data.remote.stringSuspending
import com.google.gson.Gson

class FoodRepository(private val apiService: ApiService) {

    fun getGoodFoods(token: String): LiveData<Result<FoodResponse?>> = liveData {
        emit(Result.Loading)
        try {
            val returnValue = MutableLiveData<Result<FoodResponse?>>()
            val response = apiService.getGoodFoods(ApiService.bearerToken(token))
            if(response.isSuccessful) {
                returnValue.value = Result.Success(response.body())
                emitSource(returnValue)
            } else {
                val error = Gson().fromJson(response.errorBody()?.stringSuspending(), FoodResponse::class.java)
                response.errorBody()?.close()
                returnValue.value = Result.Success(error)
                emitSource(returnValue)
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