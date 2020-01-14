package com.hobeez.sourcerise.ui.login

import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.hobeez.sourcerise.R
import com.hobeez.sourcerise.ui.util.BaseActivity
import com.hobeez.sourcerise.ui.util.DialogUtil
import com.hobeez.sourcerise.util.extensions.isValidPhoneNumber
import com.hobeez.sourcerise.util.firebase.AuthUtil
import org.jetbrains.anko.toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class LoginActivity : BaseActivity() {

    lateinit var navController: NavController
    val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (AuthUtil.getCurrentUser() != null) {
            AuthUtil.signOut()
        }

        navController = findNavController(R.id.login_nav_host)

        receiveConfirmEmailDeepLink()
    }

    private fun receiveConfirmEmailDeepLink() {
        FirebaseDynamicLinks.getInstance()
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                Timber.i("Deep Link Callback : Success.")

                // Get deep link from result (may be null if no link is found)
                var deepLink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                    navController.navigate(R.id.action_confirm_email)
                }
            }
            .addOnFailureListener(this) { e -> Timber.w("Deep Link Callback : Error - $e") }
    }

    override fun onBackPressed() {
        if (navController.currentDestination?.id != R.id.loginHomeFragment) {
            DialogUtil.simpleDialog(this,
                null,
                getString(R.string.warning_message_leave_login),
                getString(R.string.dialog_yes_button),
                DialogInterface.OnClickListener {_, _ ->
                    end()
                },
                getString(R.string.dialog_no_button),
                DialogInterface.OnClickListener {dialog, _ ->
                    dialog.dismiss()
                }
            )
        } else {
            end()
        }
    }

    fun end() {
        // Additional action
        finish()
    }

    fun onSignInSuccess() {
        toast(R.string.toast_sign_in_success)
        if(AuthUtil.getCurrentUser()?.phoneNumber == null) {
            navController.navigate(R.id.action_global_loginModaleLinkPhone)
        } else {
            end()
        }
    }

    fun onSignInFailed(message: String?) {
        toast(message ?: getString(R.string.toast_error_authentication))
    }

    fun onSignUpSuccess() {
        toast(R.string.toast_sign_up_success)
        if(AuthUtil.getCurrentUser()?.phoneNumber == null) {
            navController.navigate(R.id.action_global_loginModaleLinkPhone)
        } else {
            end()
        }
    }

    fun onSignUpFailed() {
        toast(getString(R.string.toast_sign_up_failed))
    }

    fun loginUserWithPhone() {
        val phoneNumber = AuthUtil.getPhoneNumber(this)
        if (phoneNumber == null || !phoneNumber.isValidPhoneNumber()) {
            navController.navigate(R.id.action_enter_phone_number)
        } else {
            loginViewModel.phoneNumber = phoneNumber
            AuthUtil.sendPhoneVerificationCode(this, phoneNumber)
            navController.navigate(R.id.action_with_phone_number)
        }
    }
}
