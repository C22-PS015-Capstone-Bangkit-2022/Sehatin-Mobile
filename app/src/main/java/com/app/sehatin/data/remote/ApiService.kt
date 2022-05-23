package com.app.sehatin.data.remote

import com.app.sehatin.data.model.Disease
import retrofit2.Response
import retrofit2.http.GET

//ENDPOINT
const val DISEASE_ENDPOINT = "disease"


interface ApiService {

    @GET(DISEASE_ENDPOINT)
    suspend fun getDiseases(): Response<List<Disease>>

}