package com.app.sehatin.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.app.sehatin.data.Result
import com.app.sehatin.data.remote.ApiService
import com.app.sehatin.data.remote.response.ArticlesResponse

class ArticleRepository(private val apiService: ApiService) {

    fun getArticles(page: Int, size: Int): LiveData<Result<ArticlesResponse?>> = liveData {
        emit(Result.Loading)
        try {
            val returnValue = MutableLiveData<Result<ArticlesResponse?>>()
            val response = apiService.getArticles(page, size)
            if(response.isSuccessful) {
                returnValue.value = Result.Success(response.body())
                emitSource(returnValue)
            } else {
                emit(Result.Error("Something went error"))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.toString()))
        }
    }

}