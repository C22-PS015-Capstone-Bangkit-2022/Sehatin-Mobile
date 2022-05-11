package com.app.sehatin.data.remote

import com.app.sehatin.data.model.Exercise
import com.app.sehatin.data.remote.response.FoodResponse
import com.app.sehatin.data.remote.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

//ENDPOINT
const val LOGIN_ENDPOINT = ""
const val REGISTER_ENDPOINT = ""
const val FOOD_ENDPOINT = ""
const val DISEASE_ENDPOINT = ""

//FIELD
const val USERID_FIELD = "userId"
const val EMAIL_FIELD = "email"
const val PASSWORD_FIELD = "password"

interface ApiService {

    @FormUrlEncoded
    @POST(LOGIN_ENDPOINT)
    suspend fun login(
        @Field(EMAIL_FIELD) email: String,
        @Field(PASSWORD_FIELD) password: String
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST(REGISTER_ENDPOINT)
    suspend fun register(
        @Field(EMAIL_FIELD) email: String,
        @Field(PASSWORD_FIELD) password: String
    ): Response<LoginResponse>

    @FormUrlEncoded
    @GET(FOOD_ENDPOINT)
    suspend fun getFood(
        @Field(USERID_FIELD) userId: String
    ): Response<FoodResponse>

    @FormUrlEncoded
    @GET(DISEASE_ENDPOINT)
    suspend fun getExercises(
        @Field(USERID_FIELD) userId: String
    ): Response<List<Exercise>>

}