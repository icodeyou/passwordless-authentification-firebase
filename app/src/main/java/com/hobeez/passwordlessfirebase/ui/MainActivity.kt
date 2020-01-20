package com.hobeez.passwordlessfirebase.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.hobeez.passwordlessfirebase.R
import com.hobeez.passwordlessfirebase.ui.login.LoginActivity
import com.hobeez.passwordlessfirebase.ui.util.BaseActivity
import com.hobeez.passwordlessfirebase.util.firebase.AuthUtil
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import org.koin.core.KoinComponent

class MainActivity : BaseActivity(), KoinComponent {

    // live data to make sure it gets restored with activity configuration change (rotation...)
    private var currentNavController: LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        updateLoginLogoutButtonsVisibilities()

        btLogin.setOnClickListener {
            startActivity<LoginActivity>()
        }

        btSignOut.setOnClickListener {
            AuthUtil.signOut()
            updateLoginLogoutButtonsVisibilities()
        }
    }

    override fun onResume() {
        super.onResume()
        updateLoginLogoutButtonsVisibilities()
    }

    private fun updateLoginLogoutButtonsVisibilities() {
        btLogin.visibility = if (AuthUtil.getCurrentUser() == null) View.VISIBLE else View.GONE
        btSignOut.visibility = if (AuthUtil.getCurrentUser() != null) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }
}
