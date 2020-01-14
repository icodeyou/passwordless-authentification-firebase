package com.hobeez.sourcerise.ui.util

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.hobeez.sourcerise.R
import com.hobeez.sourcerise.util.network.NoConnectivityException
import java.net.SocketTimeoutException

open class BaseFragment : androidx.fragment.app.Fragment() {

    open fun getErrorMessage(throwable: Throwable?): String {
        return when (throwable) {
            is NoConnectivityException -> getString(R.string.error_no_connectivity)
            is SocketTimeoutException -> getString(R.string.error_no_connectivity)
            else -> getString(R.string.error_default_message)
        }
    }

    fun toast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun toastError() {
        toast(getString(R.string.error_default_message))
    }

    // Permissions
    // Code inspired from :
    // https://gist.github.com/crgarridos/6e2bb91b87b078da036c2e9b14196b29#file-baseactivity-java-L35

    private val REQUEST_CODE = 1
    private var mListPermissions = arrayOf<String>()
    private var mOnPermissionGranted: () -> Unit = {}
    private var mOnPermissionDenied: () -> Unit = {}

    fun verifyStoragePermissions(
        listPermissions: Array<String>,
        onPermissionGranted: () -> Unit,
        onPermissionDenied: () -> Unit
    ) {
        mListPermissions = listPermissions // Don't forget to add permissions in manifest
        mOnPermissionGranted = onPermissionGranted
        mOnPermissionDenied = onPermissionDenied
        var permissionAreGranted = true
        for (permission in mListPermissions) {
            if (ContextCompat.checkSelfPermission(context!!, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionAreGranted = false
            }
        }
        if (!permissionAreGranted) {
            // We don't have permission so prompt the user
            requestPermissions(
                mListPermissions,
                REQUEST_CODE
            )
        } else {
            mOnPermissionGranted.invoke()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (allPermissionsAreGranted(grantResults)) {
                mOnPermissionGranted.invoke()
            } else {
                mOnPermissionDenied.invoke()
            }
        }
    }

    fun allPermissionsAreGranted(grantResults: IntArray): Boolean {
        grantResults.forEach {
            if(it != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }
}
