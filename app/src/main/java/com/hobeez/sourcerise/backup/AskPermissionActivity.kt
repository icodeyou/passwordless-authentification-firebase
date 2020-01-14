package com.hobeez.sourcerise.backup

import android.Manifest
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.hobeez.sourcerise.R
import kotlinx.android.synthetic.main.backup_activity_ask_permission.*
import org.jetbrains.anko.toast
import timber.log.Timber

private const val CODE_PERMISSION_ACCESS_FINE_LOCATION: Int = 12

class AskPermissionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.backup_activity_ask_permission)

        button_activate.setOnClickListener {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                CODE_PERMISSION_ACCESS_FINE_LOCATION
            )
        }

        button_skip.setOnClickListener {
            finish()
        }

        // To check permission, use :
        // if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == CODE_PERMISSION_ACCESS_FINE_LOCATION) {
            if (permissions.isEmpty() || grantResults.isEmpty()) {
                Timber.e("Not enough results for permission request")
                return
            }

            if (permissions.size > 1 || grantResults.size > 1) {
                Timber.e("Too many results for permission request")
                return
            }

            if (permissions.size != grantResults.size) {
                Timber.e("grant results and permissions don't match")
                return
            }

            if (grantResults[0] == PERMISSION_GRANTED) {
                toast("DEV - Permission granted")
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                toast(getString(R.string.permission_phone_denied))
            } else {
                toast(getString(R.string.permission_phone_denied))
            }
        }
    }
}
