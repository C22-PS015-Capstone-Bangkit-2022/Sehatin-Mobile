package com.app.sehatin.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.app.sehatin.data.remote.ApiService
import com.app.sehatin.data.Result
import com.app.sehatin.data.remote.response.ExerciseResponse
import com.app.sehatin.data.remote.stringSuspending
import com.google.gson.Gson

class ExerciseRepository(private val apiService: ApiService) {

    fun getGoodExercises(token: String): LiveData<Result<ExerciseResponse?>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getGoodExercises(ApiService.bearerToken(token))
            if(response.isSuccessful) {
                emitSource(MutableLiveData(Result.Success(response.body())))
            } else {
                val error = Gson().fromJson(response.errorBody()?.stringSuspending(), ExerciseResponse::class.java)
                response.errorBody()?.close()
                emitSource(MutableLiveData(Result.Success(error)))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.toString()))
        }
    }

}