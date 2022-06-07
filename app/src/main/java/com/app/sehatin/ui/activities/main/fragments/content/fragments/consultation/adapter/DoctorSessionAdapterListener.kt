package com.app.sehatin.ui.activities.main.fragments.content.fragments.consultation.adapter

import com.app.sehatin.data.model.DoctorActiveSession

interface DoctorSessionAdapterListener {

    fun onActiveClick(doctorActiveSession: DoctorActiveSession)

}