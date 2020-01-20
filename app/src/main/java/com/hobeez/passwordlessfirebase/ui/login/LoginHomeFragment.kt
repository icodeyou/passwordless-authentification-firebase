package com.hobeez.passwordlessfirebase.ui.login

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.hobeez.passwordlessfirebase.R
import com.hobeez.passwordlessfirebase.ui.util.BaseFragment
import kotlinx.android.synthetic.main.fragment_login_home.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

const val CODE_PERMISSION_PHONE: Int = 12

class LoginHomeFragment : BaseFragment() {

    private val loginViewModel: LoginViewModel by sharedViewModel()
    private lateinit var loginActivity: LoginActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loginActivity = activity as LoginActivity
        return inflater.inflate(R.layout.fragment_login_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonPhoneLogin.setOnClickListener {
            verifyStoragePermissions(
                arrayOf(
                    Manifest.permission.READ_PHONE_STATE
                ),
                onPermissionGranted = {
                    loginActivity.loginUserWithPhone()
                },
                onPermissionDenied = {
                    toast(getString(R.string.permission_phone_denied))
                }
            )
        }

        buttonEmailLogin.setOnClickListener {
            view.findNavController().navigate(R.id.action_login_email)
        }
    }

}
