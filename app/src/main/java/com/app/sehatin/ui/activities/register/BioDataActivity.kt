package com.app.sehatin.ui.activities.register

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.app.sehatin.R
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.User
import com.app.sehatin.databinding.ActivityBiodataBinding
import com.app.sehatin.ui.activities.main.MainActivity
import com.app.sehatin.ui.viewmodel.ViewModelFactory
import com.app.sehatin.utils.FileHelper
import com.google.android.material.datepicker.MaterialDatePicker
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class BioDataActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBiodataBinding
    private var selectedImageFile: File? = null
    private var selectedDate: String? = null
    private var selectedGender: Int? = null
    private var email = ""
    private var password = ""
    private lateinit var authenticationViewModel: AuthenticationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBiodataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initVariable()
        initListener()
    }

    private fun initVariable() = with(binding) {
        authenticationViewModel = ViewModelProvider(this@BioDataActivity, ViewModelFactory.getInstance())[AuthenticationViewModel::class.java]
        val genderList = resources.getStringArray(R.array.gender_list)
        val genderAdapter = ArrayAdapter(this@BioDataActivity, com.google.android.material.R.layout.support_simple_spinner_dropdown_item,genderList)
        genderInput.setAdapter(genderAdapter)
        email = intent.getStringExtra(EXTRA_EMAIL) as String
        password = intent.getStringExtra(EXTRA_PASSWORD) as String
    }

    private fun initListener() = with(binding) {
        submitBtn.setOnClickListener {
            if(isInputClear()) {
                val userData = mapOf<String, Any?>(
                    User.USERNAME to usernameInput.text.toString(),
                    User.EMAIL to email,
                    User.DATE_OF_BIRTH to dateOfBirthInput.text.toString(),
                    User.GENDER to selectedGender as Int,
                    User.IMAGE_URL to selectedImageFile
                )
                authenticationViewModel.register(email, password, userData)
            }
        }
        userImage.setOnClickListener {
            startGallery()
        }
        choosePhoto.setOnClickListener {
            startGallery()
        }
        dateOfBirthInput.setOnClickListener {
            openDatePicker()
        }
        genderInput.setOnItemClickListener { _, _, i, _ ->
            selectedGender = i
        }

        authenticationViewModel.registerState.observe(this@BioDataActivity) {
            when(it) {
                is Result.Loading -> {
                    Toast.makeText(this@BioDataActivity, "Loading", Toast.LENGTH_SHORT).show()
                }
                is Result.Error -> {
                    Toast.makeText(this@BioDataActivity, it.error, Toast.LENGTH_SHORT).show()
                }
                is Result.Success -> {
                    startActivity(Intent(this@BioDataActivity, MainActivity::class.java))
                    finish()
                }
            }
        }
    }

    private fun isInputClear(): Boolean = with(binding) {
        var valid = true
        usernameInputLayout.error = null
        dateOfBirth.error = null
        genderLayout.error = null

        if(usernameInput.text.toString().isEmpty()) {
            usernameInputLayout.error = resources.getString(R.string.error_username_input)
            valid = false
        }
        if(selectedDate == null) {
            dateOfBirth.error = resources.getString(R.string.error_selectedDate_input)
            valid = false
        }
        if(selectedGender == null) {
            genderLayout.error = resources.getString(R.string.error_gender_input)
            valid = false
        }

        return valid
    }

    private fun openDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()
        datePicker.show(supportFragmentManager, "DATE")
        datePicker.addOnPositiveButtonClickListener {
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.US)
            val calendar: Calendar = Calendar.getInstance()
            calendar.timeInMillis = it
            val date = formatter.format(calendar.time)
            selectedDate = date.toString()
            binding.dateOfBirthInput.setText(date.toString())
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.choose_photo))
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            selectedImageFile = FileHelper.reduceFileImage(FileHelper.uriToFile(selectedImg, this@BioDataActivity))
            binding.userImage.setImageURI(selectedImg)
        }
    }

    companion object {
        const val EXTRA_EMAIL = "email"
        const val EXTRA_PASSWORD = "password"
        const val TAG = "BioDataActivity"
    }

}