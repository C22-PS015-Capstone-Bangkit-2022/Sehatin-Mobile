package com.app.sehatin.ui.activities.main.fragments.diagnosis

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.sehatin.data.model.Disease
import com.app.sehatin.data.model.ScreeningQuestion
import com.app.sehatin.data.repository.DiseaseRepository

class DiagnosisViewModel(private val diseaseRepository: DiseaseRepository): ViewModel() {
    var currentAction = ANSWER_QUESTION
    var screeningQuestions = mutableListOf<ScreeningQuestion>()
    var diseases = listOf<Disease>()

    private var _counter = MutableLiveData(0)
    var counter: LiveData<Int> = _counter

    fun resetCounter() {
        _counter.value = 0
    }

    fun incrementCounter() {
        val currentValue = _counter.value
        if (currentValue != null) {
            _counter.value = (currentValue+1)
        }
    }

    fun decrementCounter() {
        val currentValue = _counter.value
        if (currentValue != null) {
            _counter.value = (currentValue-1)
        }
    }

    fun getDiseases() = diseaseRepository.getDiseases()

}