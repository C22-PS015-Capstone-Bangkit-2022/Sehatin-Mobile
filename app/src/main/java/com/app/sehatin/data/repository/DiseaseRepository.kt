package com.app.sehatin.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.app.sehatin.data.remote.ApiService
import com.app.sehatin.data.remote.response.DiseaseResponse
import com.app.sehatin.data.Result
import java.lang.Exception

class DiseaseRepository(private val apiService: ApiService) {

    fun getDiseases() : LiveData<Result<DiseaseResponse?>> = liveData {
        emit(Result.Loading)
        try {
            val returnValue = MutableLiveData<Result<DiseaseResponse?>>()
            val response = apiService.getDiseases()
            if(response.isSuccessful) {
                returnValue.value = Result.Success(response.body())
                emitSource(returnValue)
            } else {
                emit(Result.Error("Error"))
            }
        } catch (e : Exception) {
            e.localizedMessage?.let { msg ->
                emit(Result.Error(msg))
            }
        }
    }

}