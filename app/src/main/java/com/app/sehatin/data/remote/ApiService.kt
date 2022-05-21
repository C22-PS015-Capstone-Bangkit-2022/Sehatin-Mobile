package com.app.sehatin.data.remote

import com.app.sehatin.data.model.Exercise
import com.app.sehatin.data.remote.response.DiseaseResponse
import com.app.sehatin.data.remote.response.FoodResponse
import com.app.sehatin.data.remote.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

//ENDPOINT
const val DISEASE_ENDPOINT = "disease"


interface ApiService {

    @GET(DISEASE_ENDPOINT)
    suspend fun getDiseases(): Response<DiseaseResponse>

}