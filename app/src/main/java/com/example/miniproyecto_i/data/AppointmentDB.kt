package com.example.miniproyecto_i.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.miniproyecto_i.model.Appointment
import com.example.miniproyecto_i.utils.Constants.NAME_BD

@Database(entities = [Appointment::class], version = 1)
abstract class AppointmentDB : RoomDatabase() {
    abstract fun appointmentDao(): AppointmentDao

    companion object {
        fun getDatabase(context: Context): AppointmentDB {
            return Room.databaseBuilder(
                context.applicationContext,
                AppointmentDB::class.java,
                NAME_BD
            ).build()
        }
    }
}