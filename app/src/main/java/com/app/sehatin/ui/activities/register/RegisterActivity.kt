package com.app.sehatin.ui.activities.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.app.sehatin.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initVariable()
        initListener()
    }

    private fun initVariable() {

    }

    private fun initListener() = with(binding) {
        loginBtn.setOnClickListener {
            onBackPressed()
        }
        registerBtn.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, ChooseDiseaseActivity::class.java))
            finish()
        }
    }
}