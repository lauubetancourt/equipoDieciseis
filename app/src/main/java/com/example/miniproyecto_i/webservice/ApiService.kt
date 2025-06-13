package com.example.miniproyecto_i.webservice

import com.example.miniproyecto_i.model.BreedsModelResponse
import com.example.miniproyecto_i.model.BreedsPhotoModelResponse
import com.example.miniproyecto_i.utils.Constants.BREEDS_END_POINT
import com.example.miniproyecto_i.utils.Constants.BREEDS_PHOTO_END_POINT
import com.example.miniproyecto_i.utils.Constants.SUB_BREED_PHOTO_END_POINT
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET(BREEDS_END_POINT)
    suspend fun getBreeds(): BreedsModelResponse

    @GET(BREEDS_PHOTO_END_POINT)
    suspend fun getBreedPhoto(
        @Path("breed") breed: String
    ): BreedsPhotoModelResponse

    @GET(SUB_BREED_PHOTO_END_POINT)
    suspend fun getSubBreedPhoto(
        @Path("breed") breed: String,
        @Path("subBreed") subBreed: String
    ): BreedsPhotoModelResponse
}