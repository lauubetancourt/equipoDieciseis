package com.example.miniproyecto_i.model

import com.google.gson.annotations.SerializedName

data class BreedsModelResponse(
    @SerializedName("message")
    val breeds: Map<String, List<String>>
)