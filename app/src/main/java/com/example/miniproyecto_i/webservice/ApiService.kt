package com.example.miniproyecto_i.webservice

import com.example.miniproyecto_i.model.BreedsModelResponse
import com.example.miniproyecto_i.utils.Constants.BREEDS_END_POINT
import retrofit2.http.GET

interface ApiService {
    @GET(BREEDS_END_POINT)
    suspend fun getBreeds(): BreedsModelResponse
}