package com.app.sehatin.ui.activities.main.fragments.userDiseases

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.User
import com.app.sehatin.databinding.FragmentUserDiseasesBinding
import com.app.sehatin.ui.viewmodel.ViewModelFactory

class UserDiseasesFragment : Fragment() {
    private lateinit var binding : FragmentUserDiseasesBinding
    private lateinit var viewModel: UserDiseasesViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d(TAG, "onCreateView: ")
        binding = FragmentUserDiseasesBinding.inflate(inflater, container, false)
        initVariable()
        initData()
        return binding.root
    }

    private fun initVariable() {
        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance())[UserDiseasesViewModel::class.java]
    }

    private fun initData() {
        val diseasesId = User.currentUser?.diseases
        if(diseasesId != null) {
            getData(diseasesId.toString().removePrefix("[").removeSuffix("]"))
        }
    }

    private fun getData(diseasesId: String) {
        Log.d(TAG, "getData: $diseasesId")
        viewModel.getDiseasesById(diseasesId).observe(viewLifecycleOwner) {
            when(it) {
                is Result.Loading -> {
                    Log.d(TAG, "getDiseasesById loading")
                }
                is Result.Error -> {
                    Log.e(TAG, "getDiseasesById error : ${it.error}")
                }
                is Result.Success -> {
                    Log.d(TAG, "getDiseasesById success : ${it.data}")
                }
            }
        }
    }

    private companion object {
        const val TAG = "UserDiseasesFragment"
    }

}