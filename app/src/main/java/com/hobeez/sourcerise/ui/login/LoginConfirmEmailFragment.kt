package com.hobeez.sourcerise.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.hobeez.sourcerise.R
import com.hobeez.sourcerise.ui.util.BaseFragment
import com.hobeez.sourcerise.util.extensions.isEmailValid
import com.hobeez.sourcerise.util.firebase.AuthUtil
import kotlinx.android.synthetic.main.fragment_login_confirm_email.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class LoginConfirmEmailFragment : BaseFragment() {

    private val loginViewModel: LoginViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login_confirm_email, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btConfirmEmail.setOnClickListener {
            val emailLink = activity?.intent?.data?.toString()
            val email = etConfirmEmail.text.toString()

            if (!email.isEmailValid()) {
                toast(getString(R.string.toast_invalid_format_email))
                return@setOnClickListener
            }

            if (emailLink != null) {
                AuthUtil.signInWithEmail(
                    emailLink,
                    email,
                    onSuccessNewUser = { uid ->
                        val bundle = bundleOf("uid" to uid, "email" to email)
                        findNavController().navigate(R.id.action_new_user_form, bundle)
                    },
                    onSuccessExistingUser = {
                        toast(getString(R.string.toast_sign_in_success))
                        (activity as LoginActivity).onSignInSuccess()
                    },
                    onFailLinkExpired = {
                        toast(getString(R.string.toast_login_fail_link_expired))
                    },
                    onFailWrongEmail = {
                        toast(getString(R.string.toast_login_fail_email_dont_match))
                    },
                    onFailDefault = {
                        toastError()
                    }
                )
            } else {
                toastError()
                Timber.e("Can't get email link")
            }
        }
    }
}
