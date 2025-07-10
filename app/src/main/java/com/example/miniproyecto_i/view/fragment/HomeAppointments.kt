package com.example.miniproyecto_i.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.miniproyecto_i.R
import com.example.miniproyecto_i.data.AppointmentDB
import com.example.miniproyecto_i.databinding.FragmentHomeAppointmentsBinding
import com.example.miniproyecto_i.view.adapters.AppointmentAdapter
import com.example.miniproyecto_i.viewmodel.AppointmentViewModel
import kotlinx.coroutines.launch

class HomeAppointments : Fragment() {

    private lateinit var binding: FragmentHomeAppointmentsBinding
    private lateinit var recyclerView: RecyclerView
    private val appointmentViewModel: AppointmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeAppointmentsBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerViewAppointments
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        addAppointment()
        observeStatus()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appointmentViewModel.getListAppointment()

        appointmentViewModel.listAppointment.observe(viewLifecycleOwner) { appointments ->

            recyclerView.adapter = AppointmentAdapter(appointments) { appointment ->
                val bundle = Bundle().apply {
                    putSerializable("dataAppointment", appointment)
                }
                findNavController().navigate(
                    R.id.action_homeAppointments_to_detailAppointmentFragment,
                    bundle
                )
            }
        }

    }


    private fun addAppointment() {
        val addButton = binding.BtnAdd

        addButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeAppointments_to_addAppointmentFragment)
        }

    }

    private fun observeStatus() {
        appointmentViewModel.progresState.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }
}