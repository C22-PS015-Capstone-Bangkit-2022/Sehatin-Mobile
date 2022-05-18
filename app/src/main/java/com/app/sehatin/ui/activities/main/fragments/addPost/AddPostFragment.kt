package com.app.sehatin.ui.activities.main.fragments.addPost

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.app.sehatin.R
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.User
import com.app.sehatin.databinding.FragmentAddPostBinding
import com.app.sehatin.ui.viewmodel.PostViewModel
import com.app.sehatin.ui.custom.ProgressDialog
import com.app.sehatin.ui.viewmodel.ViewModelFactory
import com.app.sehatin.utils.DateHelper
import com.app.sehatin.utils.FileHelper
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File


class AddPostFragment : Fragment() {
    private lateinit var binding: FragmentAddPostBinding
    private var selectedImageFile: File? = null
    private lateinit var postViewModel: PostViewModel
    private var isPostBtnClicked = false
    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAddPostBinding.inflate(inflater, container, false)
        initVariable()
        initListener()
        DateHelper.getCurrentDate()
        return binding.root
    }

    private fun initVariable() = with(binding) {
        progressDialog = ProgressDialog(requireActivity())
        postViewModel = ViewModelProvider(this@AddPostFragment, ViewModelFactory.getInstance())[PostViewModel::class.java]
        Glide.with(requireContext())
            .load(User.currentUser?.imageUrl)
            .placeholder(R.drawable.user_default)
            .into(userImage)

        val genres = arrayOf("Stroke", "Diabetes", "Asma", "Kanker", "Ginjal")
        for (str in genres) {
            val chip = layoutInflater.inflate(R.layout.item_chip, chipsGroup, false) as Chip
            chip.text = str
            chipsGroup.addView(chip)
        }
    }

    private fun initListener() = with(binding) {
        postContent.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                postBtn.isEnabled = !text.isNullOrEmpty()
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
        })

        closeBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        addImageBtn.setOnClickListener {
            startGallery()
        }

        undoImageBtn.setOnClickListener {
            selectedImageFile = null
            postImageGroup.visibility = View.GONE
            addImageBtn.visibility = View.VISIBLE
        }

        postBtn.setOnClickListener {
            if(!isPostBtnClicked) {
                postViewModel.uploadPost(selectedImageFile, postContent.text.toString(), getTags())
                isPostBtnClicked = true
            }
        }

        postViewModel.uploadPostState.observe(viewLifecycleOwner) {
            when(it) {
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Error -> {
                    showLoading(false)
                    Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                }
                is Result.Success -> {
                    showLoading(false, isSuccess = true)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean, isSuccess: Boolean? = null) = lifecycleScope.launch {
        if(isLoading) {
            progressDialog.startLoadingDialog()
        } else {
            delay(1500)
            progressDialog.dismissDialog()
            if(isSuccess != null && isSuccess) {
                requireActivity().onBackPressed()
            }
        }
    }

    private fun getTags(): List<String>? = with(binding) {
        val tags = mutableListOf<String>()
        for(chipId in chipsGroup.checkedChipIds) {
            val chip = chipsGroup.findViewById<Chip>(chipId)
            tags.add(chip.text.toString())
        }

        if(tags.isEmpty()) {
            return null
        }
        return tags
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
            binding.postImageGroup.visibility = View.VISIBLE
            binding.addImageBtn.visibility = View.GONE
            binding.postImage.setImageURI(selectedImg)
        }
    }

}