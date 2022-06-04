package com.app.sehatin.data.repository

import androidx.lifecycle.MutableLiveData
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.Doctor
import com.app.sehatin.injection.Injection

class DoctorRepository {
    private val doctorRef = Injection.provideDoctorCollection()

    fun getDoctors(getDoctorState: MutableLiveData<Result<List<Doctor>>>) {
        getDoctorState.value = Result.Loading
        doctorRef
            .whereEqualTo(Doctor.AVAILABLE, true)
            .get()
            .addOnSuccessListener { docs ->
                val doctors = mutableListOf<Doctor>()
                for(doc in docs) {
                    val doctor = doc.toObject(Doctor::class.java)
                    doctors.add(doctor)
                }
                getDoctorState.value = Result.Success(doctors)
            }
            .addOnFailureListener {
                it.localizedMessage?.let { msg ->
                    getDoctorState.value = Result.Error(msg)
                }
            }
    }

}