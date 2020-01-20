package com.hobeez.passwordlessfirebase.ui.util

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface

class DialogUtil {
    companion object {
        fun simpleDialog(
            activity: Activity,
            title: String?,
            message: String,
            okButtonTitle: String?,
            okButtonAction: DialogInterface.OnClickListener?,
            cancelButtonTitle: String?,
            cancelButtonAction: DialogInterface.OnClickListener?
            ): AlertDialog {

            val builder: AlertDialog.Builder = activity.let {
                AlertDialog.Builder(it)
            }

            builder.setTitle(title)
                .setMessage(message)

            builder.setPositiveButton(okButtonTitle, okButtonAction)
            builder.setNegativeButton(cancelButtonTitle, cancelButtonAction)

            val dialog = builder.create()
            dialog.show()
            return dialog
        }
    }
}