package com.app.sehatin.data.remote

import com.app.sehatin.data.model.Disease
import com.app.sehatin.data.model.Food
import com.app.sehatin.data.model.User
import com.app.sehatin.data.remote.response.ArticlesResponse
import com.app.sehatin.data.remote.response.FoodResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

//ENDPOINT
const val DISEASE_ENDPOINT = "disease"
const val ARTICLE_ENDPOINT = "articles"
const val GOOD_FOOD_ENDPOINT = "disease/my/goodFood"

const val PAGE = "page"
const val SIZE = "size"
const val AUTHORIZATION = "Authorization"
const val BEARER = "Bearer "

@Suppress("BlockingMethodInNonBlockingContext")
suspend fun ResponseBody.stringSuspending() = withContext(Dispatchers.IO) { string() }

interface ApiService {

    @GET(GOOD_FOOD_ENDPOINT)
    suspend fun getGoodFoods(
        @Header(AUTHORIZATION) token : String,
    ): Response<FoodResponse>

    @GET(DISEASE_ENDPOINT)
    suspend fun getDiseases(): Response<List<Disease>>

    @GET(ARTICLE_ENDPOINT)
    suspend fun getArticles(
        @Query(PAGE) page: Int,
        @Query(SIZE) size: Int
    ): Response<ArticlesResponse>

    companion object {
        fun bearerToken(token: String): String {
            return BEARER + token + "asd"
        }
    }

}