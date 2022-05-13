package com.app.sehatin.ui.activities.main.fragments.addPost

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.app.sehatin.R
import com.app.sehatin.data.model.User
import com.app.sehatin.databinding.FragmentAddPostBinding
import com.app.sehatin.utils.FileHelper
import com.bumptech.glide.Glide
import java.io.File

class AddPostFragment : Fragment() {
    private lateinit var binding: FragmentAddPostBinding
    private var selectedImageFile: File? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAddPostBinding.inflate(inflater, container, false)
        initVariable()
        initListener()
        return binding.root
    }

    private fun initVariable() = with(binding) {
        Glide.with(requireContext())
            .load(User.currentUser?.imageUrl)
            .placeholder(R.drawable.user_default)
            .into(userImage)
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