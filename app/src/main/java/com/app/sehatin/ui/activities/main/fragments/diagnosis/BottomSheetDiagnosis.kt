package com.app.sehatin.ui.activities.main.fragments.diagnosis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.sehatin.R
import com.app.sehatin.databinding.DialogBottomSheetDiagnosisBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetDiagnosis(private val onClickListener: OnClickListener) : BottomSheetDialogFragment() {
    private lateinit var binding : DialogBottomSheetDiagnosisBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DialogBottomSheetDiagnosisBinding.bind(inflater.inflate(R.layout.dialog_bottom_sheet_diagnosis, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        selectDiseasesBtn.setOnClickListener {
            dismiss()
            onClickListener.onSelectDiseaseClicked()
        }
    }

    interface OnClickListener {
        fun onSelectDiseaseClicked()
    }

    companion object {
        const val TAG = "BottomSheetDiagnosis"
    }
}