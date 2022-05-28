package com.app.sehatin.ui.activities.main.fragments.searchUserAndTag

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.sehatin.data.Result
import com.app.sehatin.databinding.FragmentSearchUserAndTagBinding
import com.app.sehatin.ui.viewmodel.ViewModelFactory

class SearchUserAndTagFragment : Fragment() {
    private lateinit var binding : FragmentSearchUserAndTagBinding
    private lateinit var viewModel: SearchUserAndTagViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSearchUserAndTagBinding.inflate(inflater, container, false)
        initVariable()
        initListener()
        return binding.root
    }

    private fun initVariable() = with(binding) {
        viewModel = ViewModelProvider(this@SearchUserAndTagFragment, ViewModelFactory.getInstance())[SearchUserAndTagViewModel::class.java]
        searchBar.requestFocus()
        rvUser.setHasFixedSize(true)
        rvUser.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initListener() = with(binding) {
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(keyword: Editable?) {
                if(!keyword.isNullOrEmpty()) {
                    viewModel.searchUser(keyword.toString())
                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
        viewModel.searchUserState.observe(viewLifecycleOwner) {
            when(it) {
                is Result.Loading -> {
                    Log.d(TAG, "searchUserState: Loading")
                }
                is Result.Error -> {
                    Log.e(TAG, "searchUserState: error = ${it.error}")
                }
                is Result.Success -> {
                    Log.d(TAG, "searchUserState: success = ${it.data.size} : ${it.data}")
                }
            }
        }
    }

    private companion object {
        const val TAG = "SearchUserAndTagFragment"
    }

}