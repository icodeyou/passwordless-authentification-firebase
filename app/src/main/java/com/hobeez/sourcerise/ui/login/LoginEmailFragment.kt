package com.hobeez.sourcerise.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hobeez.sourcerise.R
import com.hobeez.sourcerise.ui.util.BaseFragment
import com.hobeez.sourcerise.util.extensions.isEmailValid
import com.hobeez.sourcerise.util.firebase.AuthUtil
import kotlinx.android.synthetic.main.fragment_login_email.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LoginEmailFragment : BaseFragment() {

    private val loginViewModel: LoginViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login_email, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val isNewUser: Boolean = arguments?.getBoolean("newUser") ?: false
        tvTitle.text = getString(if (isNewUser) R.string.email_intro_new_user else R.string.email_intro_old_user)

        etEmail.requestFocus()

        btEmailLogin.setOnClickListener {
            val email = etEmail.text.toString()
            if (email.isEmailValid()) {
                AuthUtil.sendEmailToUser(
                    email,
                    { toast(getString(R.string.confirm_email_sent)) }
                )
            } else {
                toast(getString(R.string.email_not_valid))
            }
        }
    }
}
