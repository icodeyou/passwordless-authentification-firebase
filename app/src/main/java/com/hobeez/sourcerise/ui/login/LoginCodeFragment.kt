package com.hobeez.sourcerise.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.hobeez.sourcerise.R
import com.hobeez.sourcerise.ui.util.BaseFragment
import com.hobeez.sourcerise.util.extensions.isValidPhoneCode
import com.hobeez.sourcerise.util.firebase.AuthUtil
import kotlinx.android.synthetic.main.fragment_login_code.*

class LoginCodeFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login_code, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        etCode.requestFocus()

        btSendBackCode.setOnClickListener {
            val code = etCode.text.toString()
            if (code.isValidPhoneCode()) {
                AuthUtil.sendBackVerificationCode(
                    activity as LoginActivity,
                    code
                )
                view.findNavController().navigate(R.id.action_send_back_code)
            } else {
                toast(getString(R.string.error_phone_code))
            }
        }

        btResendPhoneCode.setOnClickListener {
            AuthUtil.resendPhoneVerificationCode(activity as LoginActivity)
            toast(getString(R.string.code_resent))
        }
    }
}
