package com.app.sehatin.ui.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import com.app.sehatin.R
import com.app.sehatin.databinding.DialogAlertDiseasesBinding

class DiseaseAlertDialog(private val activity: Activity, private val onDiseaseAlertDialogListener: OnDiseaseAlertDialogListener) {
    private var dialog: AlertDialog? = null
    private lateinit var binding : DialogAlertDiseasesBinding


    @SuppressLint("InflateParams")
    fun showDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        binding = DialogAlertDiseasesBinding.bind(inflater.inflate(R.layout.dialog_alert_diseases, null))
        setListener()
        builder.setView(binding.root)
        dialog = builder.create()
        dialog?.show()
    }

    private fun setListener() = with(binding) {
        positiveBtn.setOnClickListener { onDiseaseAlertDialogListener.onPositiveClick() }
        negativeBtn.setOnClickListener { onDiseaseAlertDialogListener.onNegativeClick() }
    }

    fun dismissDialog() {
        dialog?.dismiss()
    }

    interface OnDiseaseAlertDialogListener {
        fun onPositiveClick()
        fun onNegativeClick()
    }

}