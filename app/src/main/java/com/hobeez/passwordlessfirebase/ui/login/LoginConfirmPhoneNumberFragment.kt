package com.hobeez.passwordlessfirebase.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.hobeez.passwordlessfirebase.R
import com.hobeez.passwordlessfirebase.ui.util.BaseFragment
import com.hobeez.passwordlessfirebase.util.extensions.isValidPhoneNumber
import com.hobeez.passwordlessfirebase.util.firebase.AuthUtil
import kotlinx.android.synthetic.main.fragment_login_confirm_phone_number.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LoginConfirmPhoneNumberFragment : BaseFragment() {

    private val loginViewModel: LoginViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login_confirm_phone_number, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btSendPhoneCode.setOnClickListener {
            val phoneNumber = etPhone.text.toString()
            if (phoneNumber.isValidPhoneNumber()) {
                loginViewModel.phoneNumber = phoneNumber
                AuthUtil.sendPhoneVerificationCode(activity as LoginActivity, phoneNumber)
                view.findNavController().navigate(R.id.action_with_manual_phone_number)
            } else {
                toast(getString(R.string.error_phone_code))
            }
        }
    }
}
