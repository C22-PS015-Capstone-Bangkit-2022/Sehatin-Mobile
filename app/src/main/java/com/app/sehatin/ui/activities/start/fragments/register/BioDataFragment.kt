package com.app.sehatin.ui.activities.start.fragments.register

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.app.sehatin.R
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.User
import com.app.sehatin.databinding.FragmentBioDataBinding
import com.app.sehatin.ui.viewmodel.ViewModelFactory
import com.app.sehatin.utils.FileHelper
import com.google.android.material.datepicker.MaterialDatePicker
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class BioDataFragment : Fragment() {
   private lateinit var binding: FragmentBioDataBinding
    private var selectedImageFile: File? = null
    private var selectedDate: String? = null
    private var selectedGender: Int? = null
    private var email = ""
    private var password = ""
    private lateinit var authenticationViewModel: AuthenticationViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentBioDataBinding.inflate(inflater, container, false)
        initVariable()
        initListener()
        return binding.root
    }

    private fun initVariable() = with(binding) {
        authenticationViewModel = ViewModelProvider(this@BioDataFragment, ViewModelFactory.getInstance())[AuthenticationViewModel::class.java]
        val genderList = resources.getStringArray(R.array.gender_list)
        val genderAdapter = ArrayAdapter(requireContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item,genderList)
        genderInput.setAdapter(genderAdapter)
        email = BioDataFragmentArgs.fromBundle(arguments as Bundle).email
        password = BioDataFragmentArgs.fromBundle(arguments as Bundle).password
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

        authenticationViewModel.registerState.observe(viewLifecycleOwner) {
            when(it) {
                is Result.Loading -> {
                    Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                }
                is Result.Error -> {
                    Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                }
                is Result.Success -> {

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
        datePicker.show(requireActivity().supportFragmentManager, "DATE")
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
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            selectedImageFile = FileHelper.reduceFileImage(FileHelper.uriToFile(selectedImg, requireContext()))
            binding.userImage.setImageURI(selectedImg)
        }
    }

    companion object {
        const val EXTRA_EMAIL = "email"
        const val EXTRA_PASSWORD = "password"
    }

}