package com.hobeez.sourcerise.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hobeez.sourcerise.R
import com.hobeez.sourcerise.domain.entity.User
import com.hobeez.sourcerise.ui.util.BaseFragment
import com.hobeez.sourcerise.util.firebase.FirestoreUtil
import kotlinx.android.synthetic.main.fragment_login_new_user.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class LoginNewUserFragment : BaseFragment() {

    private val loginViewModel: LoginViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login_new_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val firebaseUid = arguments?.getString("uid")
        val email = arguments?.getString("email")!!
        if (firebaseUid == null) {
            Timber.e("No arguments passed to LoginNewUserFragment - Firebase UID is needed")
            Timber.e("Finishing activity")
            activity?.finish()
            return
        }

        btCreateAccount.setOnClickListener {
            FirestoreUtil
                .updateUser(
                    User(
                        birthday = etBirthday.text.toString(),
                        email = email,
                        name = etNickname.text.toString(),
                        phone = loginViewModel.phoneNumber ?: ""),
                    firebaseUid = firebaseUid)
                .addOnSuccessListener {
                    Timber.i("User with uid $firebaseUid is created/updated in database")
                    (activity as LoginActivity).onSignUpSuccess()
                }
                .addOnFailureListener {e ->
                    Timber.e("Failed updating user with UID $firebaseUid in database - $e")
                    (activity as LoginActivity).onSignUpFailed()
                }
        }
    }
}
