package com.example.miniproyecto_i.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.miniproyecto_i.model.Appointment
import com.example.miniproyecto_i.model.BreedsModelResponse
import com.example.miniproyecto_i.model.BreedsPhotoModelResponse
import com.example.miniproyecto_i.repository.AppointmentRepository
import kotlinx.coroutines.launch

class AppointmentViewModel(application: Application): AndroidViewModel(application) {
    val context = getApplication<Application>()
    private val appointmentRepository = AppointmentRepository(context)
    private val appointmentId: Int = 2
    private val _listAppointment = MutableLiveData<MutableList<Appointment>>()
    val listAppointment: LiveData<MutableList<Appointment>> get() = _listAppointment

    private val _listBreeds = MutableLiveData<BreedsModelResponse>()
    val listBreeds: LiveData<BreedsModelResponse> = _listBreeds
    val appointment: LiveData<Appointment> = appointmentRepository.getAppointmentById(appointmentId)

    private val _breedPhoto = MutableLiveData<BreedsPhotoModelResponse>()
    val breedPhoto: LiveData<BreedsPhotoModelResponse> get() = _breedPhoto


    private val _progresState = MutableLiveData(false)
    val progresState: LiveData<Boolean> = _progresState

    fun saveAppointment(appointment: Appointment) {
        viewModelScope.launch {
            _progresState.value = true
            try {
                appointmentRepository.saveAppointment(appointment)
                _progresState.value = false
            } catch (e: Exception) {
                _progresState.value = false
            }
        }
    }

    fun getListAppointment() {
        viewModelScope.launch {
            _progresState.value = true
            try {
                _listAppointment.value = appointmentRepository.getListAppointment()
                _progresState.value = false
            } catch (e: Exception) {
                _progresState.value = false
            }
        }
    }

    fun deleteAppointment(appointment: Appointment) {
        viewModelScope.launch {
            _progresState.value = true
            try {
                appointmentRepository.deleteAppointment(appointment)
                _progresState.value = false
            } catch (e: Exception) {
                _progresState.value = false
            }
        }
    }

    fun updateAppointment(appointment: Appointment) {
        viewModelScope.launch {
            _progresState.value = true
            try {
                appointmentRepository.updateAppointment(appointment)
                _progresState.value = false
            } catch (e: Exception) {
                _progresState.value = false
            }
        }
    }

    fun getListBreeds() {
        viewModelScope.launch {
            _progresState.value = true
            try {
                _listBreeds.value = appointmentRepository.getListBreeds()
                _progresState.value = false
            } catch (e: Exception) {
                _progresState.value = false
            }
        }
    }

    fun getBreedPhotoByName(breed: String) {
        viewModelScope.launch {
            _progresState.value = true
            try {
                val response = appointmentRepository.getPhotoByBreed(breed)
                _breedPhoto.value = response
            } catch (e: Exception) {
                _progresState.value = false
            }
        }
    }
}