package com.app.sehatin.data.repository

import androidx.lifecycle.MutableLiveData
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.DoctorActiveSession
import com.app.sehatin.injection.Injection
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.delay

class DoctorSessionRepository {
    private val doctorSessionRef = Injection.provideUserDoctorActiveReference()

    fun createDoctorActiveSession(createDoctorActiveSessionState: MutableLiveData<Result<DoctorActiveSession>>, doctorActiveSession: DoctorActiveSession, userId: String) {
        createDoctorActiveSessionState.value = Result.Loading
        doctorActiveSession.id?.let {
            doctorSessionRef
                .child(userId)
                .child(it)
                .setValue(doctorActiveSession)
                .addOnSuccessListener {
                    createDoctorActiveSessionState.value = Result.Success(doctorActiveSession)
                }
                .addOnFailureListener { e ->
                    createDoctorActiveSessionState.value = Result.Error(e.toString())
                }
        }
    }

    fun getDoctorSession(getDoctorSessionState: MutableLiveData<Result<List<DoctorActiveSession>>>, userId: String) {
        getDoctorSessionState.value = Result.Loading
        doctorSessionRef
            .child(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val activeSessions = mutableListOf<DoctorActiveSession>()
                    for(snap in snapshot.children) {
                        val session = snap.getValue(DoctorActiveSession::class.java)
                        if (session != null) {
                            activeSessions.add(session)
                        }
                    }
                    getDoctorSessionState.value = Result.Success(activeSessions)
                }
                override fun onCancelled(error: DatabaseError) {
                    getDoctorSessionState.value = Result.Error(error.message)
                }
            })
    }

}