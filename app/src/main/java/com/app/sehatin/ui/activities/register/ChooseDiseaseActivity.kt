package com.app.sehatin.ui.activities.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.sehatin.data.model.Disease
import com.app.sehatin.databinding.ActivityChooseDiseaseBinding

class ChooseDiseaseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChooseDiseaseBinding
    private lateinit var askDiseasesAdapter: AskDiseasesAdapter
    private var diseases = arrayListOf(
        Disease("Asma"),
        Disease("Kanker"),
        Disease("Stroke"),
        Disease("Ginjal Kronis"),
        Disease("Diabetes Melitus"),
        Disease("Penyakit Sendi"),
        Disease("Asma"),
        Disease("Kanker"),
        Disease("Stroke"),
        Disease("Ginjal Kronis"),
        Disease("Diabetes Melitus"),
        Disease("Penyakit Sendi"),
    )
    private val selectedDiseases = mutableListOf<Disease>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseDiseaseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initVariable()
        initListener()
    }

    private fun initVariable() = with(binding) {
        askDiseasesAdapter = AskDiseasesAdapter(diseases)
        askDiseasesAdapter.setListener(object : AskDiseasesAdapter.OnItemClickListener {
            override fun onViewClick(disease: Disease, checkBox: CheckBox) {
                addOrRemoveFromSelectedDiseases(checkBox.isChecked, disease)
            }

            override fun onCheckBoxClick(disease: Disease, checkBox: CheckBox) {
                addOrRemoveFromSelectedDiseases(checkBox.isChecked, disease)
            }
        })
        rvDiseases.setHasFixedSize(true)
        rvDiseases.layoutManager = LinearLayoutManager(this@ChooseDiseaseActivity)
        rvDiseases.adapter = askDiseasesAdapter
    }

    private fun addOrRemoveFromSelectedDiseases(isSelected: Boolean, disease: Disease) {
        if(isSelected) {
            selectedDiseases.add(disease)
        } else {
            val index = selectedDiseases.indexOf(disease)
            if(index != -1) {
                selectedDiseases.removeAt(index)
            }
        }
        Toast.makeText(this, "$selectedDiseases", Toast.LENGTH_SHORT).show()
    }

    private fun initListener() {

    }
}