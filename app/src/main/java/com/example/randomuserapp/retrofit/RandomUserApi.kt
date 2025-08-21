package com.example.randomuserapp.retrofit

import com.example.randomuserapp.model.RandomUserResponse
import retrofit2.http.GET

interface RandomUserApi {
    @GET("api/")
    suspend fun getRandomUser(): RandomUserResponse
}