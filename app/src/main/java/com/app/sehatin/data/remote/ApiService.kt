package com.app.sehatin.data.remote

import com.app.sehatin.data.remote.response.FoodResponse
import retrofit2.Response

interface ApiService {

    //FOOD ENDPOINT
    suspend fun getFood(): Response<FoodResponse>

}