package com.example.miniproyecto_i.webservice

class ApiUtils {
    companion object {
        fun getApiService():ApiService{
            return RetrofitClient.getRetrofit().create(ApiService::class.java)
        }
    }
}