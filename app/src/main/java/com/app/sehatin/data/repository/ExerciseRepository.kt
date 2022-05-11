package com.app.sehatin.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.app.sehatin.data.model.Exercise
import com.app.sehatin.data.remote.ApiService
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.User

class ExerciseRepository(private val apiService: ApiService) {

    fun getExercise(): LiveData<Result<List<Exercise>?>> = liveData {
        emit(Result.Loading)
        try {
            val returnValue = MutableLiveData<Result<List<Exercise>?>>()
            val userId = User.currentUser!!.id
            val response = apiService.getExercises(userId)
            if(response.isSuccessful) {
                returnValue.value = Result.Success(response.body())
                emitSource(returnValue)
            } else {
                emit(Result.Error("No Data"))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.toString()))
        }
    }

}