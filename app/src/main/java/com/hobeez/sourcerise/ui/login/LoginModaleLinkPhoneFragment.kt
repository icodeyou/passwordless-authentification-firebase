package com.hobeez.sourcerise.ui.login

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hobeez.sourcerise.R
import com.hobeez.sourcerise.ui.util.BaseFragment
import kotlinx.android.synthetic.main.fragment_login_modale_link_phone.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LoginModaleLinkPhoneFragment : BaseFragment() {

    private val loginViewModel: LoginViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login_modale_link_phone, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        cancel_modale_link.setOnClickListener {
            (activity as LoginActivity).end()
        }

        btLinkPhoneAccount.setOnClickListener {
            verifyStoragePermissions(
                arrayOf(
                    Manifest.permission.READ_PHONE_STATE
                ),
                onPermissionGranted = {
                    (activity as LoginActivity).loginUserWithPhone()
                },
                onPermissionDenied = {
                    toast(getString(R.string.permission_phone_denied))
                }
            )
        }
    }
}
