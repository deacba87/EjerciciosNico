package com.example.myapplication

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater

class LoadingDialog(var activity: Activity) {
    var dialog: AlertDialog? = null

    fun startLoadingAnimation()
    {
        var buillder = AlertDialog.Builder(activity)
        var inflater = activity.layoutInflater

        buillder.setView(inflater.inflate(R.layout.loading_dialog, null))
        buillder.setCancelable(true)
        dialog = buillder.create()
        dialog?.show()
    }
    fun dismissDialog()
    {
        dialog?.dismiss()
    }

}