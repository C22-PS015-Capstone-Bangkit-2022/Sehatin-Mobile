package com.app.sehatin.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.app.sehatin.data.remote.ApiService
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.Disease
import com.app.sehatin.data.model.User
import com.app.sehatin.injection.Injection
import kotlin.Exception

class DiseaseRepository(private val apiService: ApiService) {
    private val userRef = Injection.provideUserCollection()

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

    fun getDiseasesById(diseasesId: List<String>) : LiveData<Result<List<Disease>?>> = liveData {
        emit(Result.Loading)
        try {
            val returnValue = MutableLiveData<Result<List<Disease>?>>()
            val response = apiService.getDiseasesById(diseasesId)
            if(response.isSuccessful) {
                returnValue.value = Result.Success(response.body())
                Log.d("getDiseasesById", "getDiseasesById: ${response.headers()}")
                emitSource(returnValue)
            } else {
                emit(Result.Error("No Data"))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.toString()))
        }
    }

    fun saveUserDiseases(saveDiseasesState: MutableLiveData<Result<List<String>>>, diseaseIds: List<String>) {
        saveDiseasesState.value = Result.Loading
        User.currentUser?.id?.let {
            userRef.document(it)
                .update(User.DISEASES, diseaseIds)
                .addOnSuccessListener {
                    saveDiseasesState.value = Result.Success(diseaseIds)
                }
                .addOnFailureListener { err ->
                    err.localizedMessage?.let { msg ->
                        saveDiseasesState.value = Result.Error(msg)
                    }
                }
        }
    }

}