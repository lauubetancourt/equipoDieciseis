package com.example.miniproyecto_i.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Appointment(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val petName: String,
    val breed: String,
    val ownerName: String,
    val ownerPhone: String,
    //val sintomas: String
    //photo
): Serializable