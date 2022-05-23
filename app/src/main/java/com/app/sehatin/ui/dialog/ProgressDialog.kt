package com.app.sehatin.ui.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import com.app.sehatin.R

class ProgressDialog(private val activity: Activity) {
    private var dialog: AlertDialog? = null

    @SuppressLint("InflateParams")
    fun startLoadingDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.dialog_progress, null))
        dialog = builder.create()
        dialog?.show()
    }

    fun dismissDialog() {
        dialog?.dismiss()
    }
}