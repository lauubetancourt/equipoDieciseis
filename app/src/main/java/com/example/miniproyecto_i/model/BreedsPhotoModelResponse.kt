package com.example.miniproyecto_i.model

import com.google.gson.annotations.SerializedName

data class BreedsPhotoModelResponse(
    @SerializedName("message")
    val photo: String
)