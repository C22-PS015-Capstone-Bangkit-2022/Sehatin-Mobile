package com.app.sehatin.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.app.sehatin.data.remote.ApiService
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.Disease
import kotlin.Exception

class DiseaseRepository(private val apiService: ApiService) {

    fun getDiseases() : LiveData<Result<List<Disease>?>> = liveData {
        emit(Result.Loading)
        try {
            val returnValue = MutableLiveData<Result<List<Disease>?>>()
            val response = apiService.getDiseases()
            if(response.isSuccessful) {
                returnValue.value = Result.Success(response.body())
                emitSource(returnValue)
            } else {
                emit(Result.Error("Error"))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.toString()))
        }
    }

}