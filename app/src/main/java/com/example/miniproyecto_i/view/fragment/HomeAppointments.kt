package com.example.miniproyecto_i.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.miniproyecto_i.R
import com.example.miniproyecto_i.data.AppointmentDB
import com.example.miniproyecto_i.view.adapters.AppointmentAdapter
import kotlinx.coroutines.launch

class HomeAppointments : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home_appointments, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewAppointments)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            val db = AppointmentDB.getDatabase(requireContext())
            val appointmentsFromDb = db.appointmentDao().getListAppointment()

            // Configurar adaptador
            recyclerView.adapter = AppointmentAdapter(appointmentsFromDb)
        }
    }
}
