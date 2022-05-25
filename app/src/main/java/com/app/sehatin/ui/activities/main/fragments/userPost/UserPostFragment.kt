package com.app.sehatin.ui.activities.main.fragments.userPost

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.sehatin.data.model.User
import com.app.sehatin.databinding.FragmentUserPostBinding
import com.app.sehatin.ui.activities.main.fragments.content.post.adapter.PostAdapterWithPaging
import com.app.sehatin.ui.viewmodel.ViewModelFactory

class UserPostFragment : Fragment() {
    private lateinit var binding: FragmentUserPostBinding
    private lateinit var postType: String
    private lateinit var viewModel: UserPostViewModel
    private val postAdapter = PostAdapterWithPaging()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentUserPostBinding.inflate(inflater, container, false)
        initVariable()
        initListener()
        initData()
        return binding.root
    }

    private fun initVariable() = with(binding) {
        postType = UserPostFragmentArgs.fromBundle(arguments as Bundle).type
        viewModel = ViewModelProvider(this@UserPostFragment, ViewModelFactory.getInstance())[UserPostViewModel::class.java]
        rvPost.setHasFixedSize(true)
        rvPost.layoutManager = LinearLayoutManager(requireContext())
        rvPost.adapter = postAdapter
    }

    private fun initListener() {

    }

    private fun initData() {
        when(postType) {
            TYPE_USER_POST -> {
                val user = User.currentUser?.id
                user?.let {
                    getUserPost(it)
                }
            }
            TYPE_SAVED_POST -> {

            }
        }
        Log.d(TAG, "initData: $postType")
    }

    private fun getUserPost(userId: String) {

    }

    companion object {
        const val TYPE_USER_POST = "user post"
        const val TYPE_SAVED_POST = "saved post"
        const val TAG = "UserPostFragment"
    }

}