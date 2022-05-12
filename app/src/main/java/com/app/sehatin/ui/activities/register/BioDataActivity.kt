package com.app.sehatin.ui.activities.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.app.sehatin.R
import com.app.sehatin.databinding.ActivityBiodataBinding
import com.app.sehatin.ui.activities.main.MainActivity

class BioDataActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBiodataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBiodataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initVariable()
        initListener()
    }

    private fun initVariable() = with(binding) {
        val genderList = resources.getStringArray(R.array.gender_list)
        val genderAdapter = ArrayAdapter(this@BioDataActivity, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, genderList)

    }

    private fun initListener() = with(binding) {
        submitBtn.setOnClickListener {
            startActivity(Intent(this@BioDataActivity, MainActivity::class.java))
            finish()
        }
    }

}