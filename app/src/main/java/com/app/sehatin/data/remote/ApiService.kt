package com.app.sehatin.data.remote

import com.app.sehatin.data.model.Disease
import com.app.sehatin.data.remote.response.ArticlesResponse
import com.app.sehatin.data.remote.response.FoodResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

//ENDPOINT
const val DISEASE_SCREENING_ENDPOINT = "disease/screening"
const val DISEASE_SEARCH_BY_ID = "disease/searchById"
const val ARTICLE_ENDPOINT = "articles"
const val GOOD_FOOD_ENDPOINT = "disease/my/goodFood"

const val PAGE = "page"
const val SIZE = "size"
const val AUTHORIZATION = "Authorization"
const val BEARER = "Bearer "
const val ID = "id_penyakit"

@Suppress("BlockingMethodInNonBlockingContext")
suspend fun ResponseBody.stringSuspending() = withContext(Dispatchers.IO) { string() }

interface ApiService {

    @GET(GOOD_FOOD_ENDPOINT)
    suspend fun getGoodFoods(
        @Header(AUTHORIZATION) token : String,
    ): Response<FoodResponse>

    @GET(DISEASE_SCREENING_ENDPOINT)
    suspend fun getDiseases(): Response<List<Disease>>

    @GET(DISEASE_SEARCH_BY_ID)
    suspend fun getDiseasesById(
        @Header(ID) diseasesId: List<String>,
    ): Response<List<Disease>>

    @GET(ARTICLE_ENDPOINT)
    suspend fun getArticles(
        @Query(PAGE) page: Int,
        @Query(SIZE) size: Int
    ): Response<ArticlesResponse>

    companion object {
        fun bearerToken(token: String): String {
            return BEARER + token
        }
    }

}