package com.app.sehatin.ui.activities.main.fragments.content.fragments.consultation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.Doctor
import com.app.sehatin.data.model.DoctorActiveSession
import com.app.sehatin.data.repository.DoctorRepository
import com.app.sehatin.data.repository.DoctorSessionRepository

class ConsultationViewModel(private val doctorRepository: DoctorRepository, private val doctorSessionRepository: DoctorSessionRepository): ViewModel() {

    val getDoctorState = MutableLiveData<Result<List<Doctor>>>()
    val getUserDoctorSessionState = MutableLiveData<Result<List<DoctorActiveSession>>>()

    fun getDoctor() = doctorRepository.getDoctors(getDoctorState)

    fun getUserDoctorSession(userId: String) = doctorSessionRepository.getDoctorSession(getUserDoctorSessionState, userId)

}