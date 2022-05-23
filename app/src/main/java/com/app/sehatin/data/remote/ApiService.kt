package com.app.sehatin.data.remote

import com.app.sehatin.data.model.Disease
import com.app.sehatin.data.remote.response.ArticlesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

//ENDPOINT
const val DISEASE_ENDPOINT = "disease"
const val ARTICLE_ENDPOINT = "articles"

//VALUE
const val PAGE = "page"
const val SIZE = "size"

interface ApiService {

    @GET(DISEASE_ENDPOINT)
    suspend fun getDiseases(): Response<List<Disease>>

    @GET(ARTICLE_ENDPOINT)
    suspend fun getArticles(
        @Query(PAGE) page: Int,
        @Query(SIZE) size: Int
    ): Response<ArticlesResponse>

}