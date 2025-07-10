package com.example.miniproyecto_i.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.miniproyecto_i.data.AppointmentDB
import com.example.miniproyecto_i.data.AppointmentDao
import com.example.miniproyecto_i.model.Appointment
import com.example.miniproyecto_i.model.BreedsModelResponse
import com.example.miniproyecto_i.model.BreedsPhotoModelResponse
import com.example.miniproyecto_i.webservice.ApiService
import com.example.miniproyecto_i.webservice.ApiUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppointmentRepository(val context: Context) {
    private var appointmentDao: AppointmentDao = AppointmentDB.getDatabase(context).appointmentDao()
    private var apiService: ApiService = ApiUtils.getApiService()

    suspend fun saveAppointment(appointment:Appointment) {
        withContext(Dispatchers.IO) {
            appointmentDao.saveAppointment(appointment)
        }
    }

    suspend fun getListAppointment(): MutableList<Appointment> {
        return withContext(Dispatchers.IO) {
            appointmentDao.getListAppointment()
        }
    }

    suspend fun deleteAppointment(appointment: Appointment) {
        withContext(Dispatchers.IO) {
            appointmentDao.deleteAppointment(appointment)
        }
    }

    suspend fun updateAppointment(appointment: Appointment) {
        withContext(Dispatchers.IO) {
            appointmentDao.updateAppointment(appointment)
        }
    }

    suspend fun getListBreeds(): BreedsModelResponse {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getBreeds()
                response
            } catch (e: Exception) {
                e.printStackTrace()
                BreedsModelResponse(breeds = emptyMap())
            }
        }
    }

    suspend fun getPhotoByBreed(breed: String): BreedsPhotoModelResponse {
        return withContext(Dispatchers.IO) {
            val parts = breed.lowercase().split(" ")
            if (parts.size == 2) {
                apiService.getSubBreedPhoto(parts[1], parts[0])
            } else {
                apiService.getBreedPhoto(parts[0])
            }
        }
    }
}