package com.app.sehatin.ui.activities.main.fragments.userDiseases

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.sehatin.R
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.User
import com.app.sehatin.databinding.FragmentUserDiseasesBinding
import com.app.sehatin.ui.activities.main.fragments.diagnosis.BottomSheetDiagnosis
import com.app.sehatin.ui.viewmodel.ViewModelFactory

class UserDiseasesFragment : Fragment() {
    private lateinit var binding: FragmentUserDiseasesBinding
    private lateinit var viewModel: UserDiseasesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView: ")
        binding = FragmentUserDiseasesBinding.inflate(inflater, container, false)
        initVariable()
        initListener()
        initData()
        return binding.root
    }

    private fun initVariable() {
        viewModel = ViewModelProvider(this@UserDiseasesFragment, ViewModelFactory.getInstance())[UserDiseasesViewModel::class.java]
    }

    private fun initListener() = with(binding) {
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        infoBtn.setOnClickListener {
            val modalBottomSheet = UserDiseasesBottomSheet {
                findNavController().navigate(R.id.action_userDiseasesFragment_to_diagnosisFragment)
            }
            modalBottomSheet.show(requireActivity().supportFragmentManager, BottomSheetDiagnosis.TAG)
        }
    }

    private fun initData() {
        val diseasesId = User.currentUser?.diseases
        if (diseasesId != null) {
            getData(diseasesId.toString().removePrefix("[").removeSuffix("]"))
        }
    }

    private fun getData(diseasesId: String) {
        Log.d(TAG, "getData: $diseasesId")
        if(viewModel.diseases.isEmpty()) {
            viewModel.getDiseasesById(diseasesId).observe(viewLifecycleOwner) {
                when (it) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Error -> {
                        Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                        Log.e(TAG, "getDiseasesById error : ${it.error}")
                    }
                    is Result.Success -> {
                        it.data?.let { diseases ->
                            viewModel.diseases.addAll(diseases)
                            showLoading(false)
                            setView()
                        }
                    }
                }
            }
        } else {
            showLoading(false)
            setView()
        }
    }

    private fun showLoading(isLoading: Boolean) = with(binding) {
        if (isLoading) {
            contentLayout.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        } else {
            contentLayout.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }

    private fun setView() = with(binding) {
        rvDiseases.setHasFixedSize(true)
        rvDiseases.layoutManager = LinearLayoutManager(requireContext())
        rvDiseases.adapter = UserDiseasesAdapter(viewModel.diseases)
    }

    private companion object {
        const val TAG = "UserDiseasesFragment"
    }

}