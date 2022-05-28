package com.app.sehatin.ui.activities.main.fragments.userDiseases

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.sehatin.R
import com.app.sehatin.databinding.DialogBottomSheetUserDiseasesBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class UserDiseasesBottomSheet(private val onActionClick: () -> Unit) : BottomSheetDialogFragment() {
    private lateinit var binding : DialogBottomSheetUserDiseasesBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DialogBottomSheetUserDiseasesBinding.bind(inflater.inflate(R.layout.dialog_bottom_sheet_user_diseases, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        actionBtn.setOnClickListener {
            dismiss()
            onActionClick()
        }
    }

    companion object {
        const val TAG = "BottomSheetDiagnosis"
    }
}