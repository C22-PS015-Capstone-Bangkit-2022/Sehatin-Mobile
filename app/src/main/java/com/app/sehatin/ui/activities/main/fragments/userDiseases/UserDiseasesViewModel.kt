package com.app.sehatin.ui.activities.main.fragments.userDiseases

import androidx.lifecycle.ViewModel
import com.app.sehatin.data.model.Disease
import com.app.sehatin.data.repository.DiseaseRepository

class UserDiseasesViewModel(private val diseaseRepository: DiseaseRepository): ViewModel() {

    val diseases = mutableListOf<Disease>()

    fun getDiseasesById(diseasesId: String) = diseaseRepository.getDiseasesById(diseasesId)

}