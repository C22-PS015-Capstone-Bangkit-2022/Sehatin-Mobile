package com.app.sehatin.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.app.sehatin.data.repository.DiseaseRepository

class DiagnosisViewModel(private val diseaseRepository: DiseaseRepository): ViewModel() {

    fun getDiseases() = diseaseRepository.getDiseases()

}