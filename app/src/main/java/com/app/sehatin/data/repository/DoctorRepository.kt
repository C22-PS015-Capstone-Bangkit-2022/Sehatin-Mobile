package com.app.sehatin.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.Doctor
import com.app.sehatin.data.remote.ApiService
import kotlinx.coroutines.delay

class DoctorRepository(private val apiService: ApiService) {

    fun getDoctors(): LiveData<Result<List<Doctor>>> = liveData {
        emit(Result.Loading)
        try {
            delay(1500L)
            emit(Result.Success(doctorList))
        } catch (e: Exception) {
            e.localizedMessage?.let { msg ->
                emit(Result.Error(msg))
            }
        }
    }

    private val doctorList = mutableListOf(
        Doctor(
            "1",
            "Dr. Ahmad Fathanah",
            123123123123,
            "Spesialis Perasaan",
            10,
            "Universitas Of Mars",
            "PT mencari cinta sejati",
            90000,
            5.0,
            50,
            "https://dental.id/wp-content/uploads/2016/03/suami.gif"
        ),
        Doctor(
            "1",
            "Dr. Ahmad Fathanah",
            123123123123,
            "Spesialis Perasaan",
            10,
            "Universitas Of Mars",
            "PT mencari cinta sejati",
            90000,
            5.0,
            50,
            "https://dental.id/wp-content/uploads/2016/03/suami.gif"
        ),
        Doctor(
            "1",
            "Dr. Ahmad Fathanah",
            123123123123,
            "Spesialis Perasaan",
            10,
            "Universitas Of Mars",
            "PT mencari cinta sejati",
            90000,
            5.0,
            50,
            "https://dental.id/wp-content/uploads/2016/03/suami.gif"
        ),
        Doctor(
            "1",
            "Dr. Ahmad Fathanah",
            123123123123,
            "Spesialis Perasaan",
            10,
            "Universitas Of Mars",
            "PT mencari cinta sejati",
            90000,
            5.0,
            50,
            "https://dental.id/wp-content/uploads/2016/03/suami.gif"
        )
    )

}