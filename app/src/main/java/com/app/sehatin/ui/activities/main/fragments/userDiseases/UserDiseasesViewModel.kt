package com.app.sehatin.ui.activities.main.fragments.userDiseases

import androidx.lifecycle.ViewModel
import com.app.sehatin.data.repository.DiseaseRepository

class UserDiseasesViewModel(private val diseaseRepository: DiseaseRepository): ViewModel() {

    fun getDiseasesById(diseasesId: String) = diseaseRepository.getDiseasesById(diseasesId)

}