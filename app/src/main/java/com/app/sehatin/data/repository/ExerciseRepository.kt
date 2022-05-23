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
            // TODO: GET EXERCISE
        } catch (e: Exception) {
            emit(Result.Error(e.toString()))
        }
    }

}