package com.app.sehatin.ui.activities.main.fragments.content.fragments.consultation

import androidx.lifecycle.ViewModel
import com.app.sehatin.data.repository.DoctorRepository

class ConsultationViewModel(private val doctorRepository: DoctorRepository): ViewModel() {

    fun getDoctor() = doctorRepository.getDoctors()

}