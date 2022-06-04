package com.app.sehatin.ui.activities.main.fragments.content.fragments.consultation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.Doctor
import com.app.sehatin.data.repository.DoctorRepository

class ConsultationViewModel(private val doctorRepository: DoctorRepository): ViewModel() {

    val getDoctorState = MutableLiveData<Result<List<Doctor>>>()

    fun getDoctor() = doctorRepository.getDoctors(getDoctorState)

}