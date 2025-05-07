package com.example.miniproyecto_i.model

data class Appointment(
    val id: String,
    val name: String,
    val breed: String,
    val notes: String,
    val ownerName: String,
    val ownerPhone: String,
    val imageUrl: String
)