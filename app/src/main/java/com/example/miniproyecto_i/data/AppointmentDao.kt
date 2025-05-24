package com.example.miniproyecto_i.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.miniproyecto_i.model.Appointment

@Dao
interface AppointmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAppointment(appointment: Appointment)

    @Query("SELECT * FROM Appointment")
    suspend fun getListAppointment(): MutableList<Appointment>

    @Delete
    suspend fun deleteAppointment(appointment: Appointment)

    @Update
    suspend fun updateAppointment(appointment: Appointment)

    @Query("SELECT * FROM Appointment WHERE id = :id LIMIT 1")
    fun getAppointmentById(id: Int): LiveData<Appointment>


}