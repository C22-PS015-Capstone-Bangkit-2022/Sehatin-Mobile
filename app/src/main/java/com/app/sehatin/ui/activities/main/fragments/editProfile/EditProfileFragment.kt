package com.app.sehatin.ui.activities.main.fragments.editProfile

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
import androidx.lifecycle.lifecycleScope
import com.app.sehatin.R
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.User
import com.app.sehatin.databinding.FragmentEditProfileBinding
import com.app.sehatin.ui.activities.start.fragments.AuthenticationViewModel
import com.app.sehatin.ui.viewmodel.ViewModelFactory
import com.app.sehatin.utils.FileHelper
import com.app.sehatin.utils.MAN
import com.app.sehatin.utils.WOMAN
import com.bumptech.glide.Glide
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class EditProfileFragment : Fragment() {
    private var _binding: FragmentEditProfileBinding? = null
    private lateinit var user: User
    private val binding get() = _binding!!
    private var selectedImageFile: File? = null
    private var selectedDate: String? = null
    private var selectedGender: Int? = null
    private lateinit var authenticationViewModel: AuthenticationViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        initVariable()
        initListener()
        return binding.root
    }

    private fun initVariable() = with(binding) {
        authenticationViewModel = ViewModelProvider(this@EditProfileFragment, ViewModelFactory.getInstance())[AuthenticationViewModel::class.java]
        user = EditProfileFragmentArgs.fromBundle(arguments as Bundle).user
        Glide.with(requireContext())
            .load(user.imageUrl)
            .placeholder(R.drawable.user_default)
            .error(R.drawable.user_default)
            .into(userImage)
        usernameInput.setText(user.username)
        val genderList = resources.getStringArray(R.array.gender_list)
        val genderAdapter = ArrayAdapter(requireContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item,genderList)
        genderInput.setAdapter(genderAdapter)

        user.dateOfBirth?.let {
            selectedDate = it
            dateOfBirthInput.setText(it)
        }
        user.gender?.let {
            if(it == MAN) {
                selectedGender = MAN
                genderInput.setText(getString(R.string.man), false)
            } else {
                selectedGender = WOMAN
                genderInput.setText(getString(R.string.woman), false)
            }
        }
    }

    private fun initListener() = with(binding) {
        editProfileToolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        submitBtn.setOnClickListener {
            if(isInputClear()) {
                val map = mutableMapOf<String, Any>(
                    User.USERNAME to usernameInput.text.toString(),
                    User.DATE_OF_BIRTH to dateOfBirthInput.text.toString(),
                    User.GENDER to selectedGender as Int
                )
                user.id?.let { userId ->
                    authenticationViewModel.updateUser(userId, selectedImageFile, map)
                }
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
        authenticationViewModel.updateUserState.observe(viewLifecycleOwner) {
            when(it) {
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Error -> {
                    showLoading(false)
                    Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                }
                is Result.Success -> {
                    lifecycleScope.launch {
                        delay(1000L)
                        Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                        requireActivity().onBackPressed()
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) = with(binding) {
        if(isLoading) {
            progressBar.progressBtn.visibility = View.VISIBLE
            submitBtn.visibility = View.GONE
        } else {
            progressBar.progressBtn.visibility = View.GONE
            submitBtn.visibility = View.VISIBLE
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

}