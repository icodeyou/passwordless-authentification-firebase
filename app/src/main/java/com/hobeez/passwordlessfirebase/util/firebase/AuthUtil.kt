package com.hobeez.passwordlessfirebase.util.firebase

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.telephony.TelephonyManager
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthActionCodeException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.hobeez.passwordlessfirebase.R
import com.hobeez.passwordlessfirebase.ui.login.LoginActivity
import org.jetbrains.anko.toast
import timber.log.Timber
import java.util.concurrent.TimeUnit

const val TIMEOUT_PHONE_AUTH_SECONDS = 30L

class AuthUtil {
    companion object {
        private var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks? = null
        private var mPhoneNumber: String? = null
        private var storedVerificationId: String? = null
        private var resendToken: PhoneAuthProvider.ForceResendingToken? = null
        private val auth = FirebaseAuth.getInstance()

        @SuppressLint("HardwareIds")
        fun getPhoneNumber(context: Context): String? {
            if (ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.READ_PHONE_STATE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val tMgr = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                val mPhoneNumber = tMgr.line1Number
                return mPhoneNumber
            } else {
                return null
            }
        }

        fun sendPhoneVerificationCode(loginActivity: LoginActivity, phoneNumber: String) {
            mPhoneNumber = phoneNumber
            mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // This callback will be invoked in two situations:
                    // 1 - Instant verification. In some cases the phone number can be instantly
                    //     verified without needing to send or enter a verification code.
                    // 2 - Auto-retrieval. On some devices Google Play services can automatically
                    //     detect the incoming verification SMS and perform verification without
                    //     user action.
                    Timber.i("Firebase Authentication completed automatically with credentials : $credential")
                    signInWithPhone(
                        loginActivity,
                        credential
                    )
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    // This callback is invoked if an invalid request for verification is made
                    Timber.w("onVerificationFailed : $e")
                    if (e is FirebaseAuthInvalidCredentialsException) {
                        Timber.e("Credentials are not correct")
                    } else if (e is FirebaseTooManyRequestsException) {
                        Timber.e("SMS quota has been exceeded")
                    }
                    loginActivity.toast(loginActivity.getString(R.string.error_login_general))
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    Timber.i(" SMS verification code has been sent to the provided phone number")

                    Timber.d("verificationId is : $verificationId")
                    Timber.d("token is : $token")

                    storedVerificationId = verificationId
                    resendToken = token // TODO use this

                    loginActivity.navController.navigate(R.id.action_code_sent_to_user)
                }
            }

            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                TIMEOUT_PHONE_AUTH_SECONDS,
                TimeUnit.SECONDS,
                loginActivity, // Activity (for callback binding)
                mCallbacks!! // OnVerificationStateChangedCallbacks
            )
        }

        fun resendPhoneVerificationCode(activity: Activity) {
            if ( mPhoneNumber != null && mCallbacks != null ) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    mPhoneNumber!!,
                    TIMEOUT_PHONE_AUTH_SECONDS,
                    TimeUnit.SECONDS,
                    activity, // Activity (for callback binding)
                    mCallbacks!!, // OnVerificationStateChangedCallbacks
                    resendToken
                )
            }
        }

        private fun signInWithPhone(
            loginActivity: LoginActivity,
            credential: PhoneAuthCredential
        ) {
            if(auth.currentUser?.email != null) {
                auth.currentUser?.linkWithCredential(credential)
                    ?.addOnCompleteListener(loginActivity) { task ->
                        if (task.isSuccessful) {
                            Timber.d( "linkWithCredential:success")
                            val user = task.result?.user
                            if(user?.uid == null) {
                                Timber.e("Failed updating user phone number - User or uid is null and shouldn't be")
                            } else {
                                FirestoreUtil.updatePhoneNumber(user.uid, auth.currentUser?.phoneNumber)
                                    ?.addOnSuccessListener {
                                        Timber.i("Firestore - Successfully updated user phone number")
                                    }
                                    ?.addOnFailureListener {e ->
                                        Timber.e("Firestore - Failed updating user phone number - $e")
                                    }
                            }
                        } else {
                            Timber.e("linkWithCredential:failure")
                            loginActivity.toast(loginActivity.getString(R.string.toast_error_authentication))
                        }
                        loginActivity.end()
                    }
            } else {
                auth.signInWithCredential(credential)
                    .addOnCompleteListener { signInTask ->
                        val result = signInTask.result
                        if (result?.user == null) {
                            Timber.e("User is null after authentication")
                            loginActivity.toast(loginActivity.getString(R.string.toast_error_authentication))
                            auth.signOut()
                            return@addOnCompleteListener
                        }
                        val phoneUser = result.user!!
                        if (signInTask.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Timber.i("signInWithPhone:success")

                            // If user does not exist in Firebase Authentication DB
                            if (result.additionalUserInfo?.isNewUser == true) {

                                // Immediately delete phone-user because we want him to be connected with email only
                                Timber.d("Current user about to be deleted : ${auth.currentUser?.phoneNumber}")
                                auth.currentUser?.delete()?.addOnCompleteListener {deleteUserTask ->
                                    if (deleteUserTask.isSuccessful()) {
                                        Timber.i("Firebase Auth User phone-account deleted.")
                                    }
                                }

                                val emailFragmentBundle = bundleOf(
                                    "newUser" to true
                                )
                                loginActivity.navController.navigate(R.id.action_email_after_phone, emailFragmentBundle)
                            } else {
                                FirestoreUtil.getUserFromUid(phoneUser.uid)
                                    .addOnSuccessListener { user ->
                                        if (user?.hasValidFields() == true) {
                                            loginActivity.onSignInSuccess()
                                        } else {
                                            val newUserFragmentBundle = bundleOf("uid" to phoneUser.uid)
                                            loginActivity.navController.navigate(R.id.action_new_user_form, newUserFragmentBundle)
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        Timber.e("Failure getting user ${phoneUser.uid} - $e")
                                        loginActivity.onSignInFailed(message = null)
                                    }
                            }
                        } else {
                            // Sign in failed, display a message and update the UI
                            Timber.w("signInWithPhone:failure : ${signInTask.exception}")
                            if (signInTask.exception is FirebaseAuthInvalidCredentialsException) {
                                Timber.i("Invalid verification code")
                                loginActivity.toast(R.string.invalid_verif_phone_code)
                            }
                        }
                    }
            }
        }

        fun sendBackVerificationCode(loginActivity: LoginActivity, code: String) {
            Timber.i("Sending back verification code")
            if (storedVerificationId != null) {
                val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, code)
                signInWithPhone(
                    loginActivity,
                    credential
                )
            } else {
                Timber.e("storedVerificationId is null and shouldn't be - Aborting authentication")
            }
        }

        fun sendEmailToUser(email: String, callback: () -> Unit) {
            Timber.d("Logging user with email")
            val actionCodeSettings = ActionCodeSettings.newBuilder()
                .setUrl("http://sourcerise-jack.firebaseapp.com")
                // This must be true
                .setHandleCodeInApp(true)
                .setAndroidPackageName(
                    "com.hobeez.sourcerise.dev",
                    true, /* installIfNotAvailable */
                    "1" /* minimumVersion */
                )
                .build()
            auth.sendSignInLinkToEmail(email, actionCodeSettings)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Timber.d("Email sent.")
                        callback.invoke()
                    } else {
                        Timber.d("An error occured while sending the email : ${task.exception}")
                    }
                }
        }

        fun signInWithEmail(
            emailLink: String,
            email: String,
            onSuccessNewUser: (uid: String) -> Unit,
            onSuccessExistingUser: () -> Unit,
            onFailLinkExpired: () -> Unit,
            onFailWrongEmail: () -> Unit,
            onFailDefault: () -> Unit
        ) {
            // Confirm the link is a sign-in with email link.
            if (auth.isSignInWithEmailLink(emailLink)) {
                // The client SDK will parse the code from the link for you.
                try {
                    auth.signInWithEmailLink(email, emailLink)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Timber.d("Successfully signed in with email link!")
                                val result = task.result
                                if (result?.user == null) {
                                    Timber.e("User is null after authentication")
                                    auth.signOut()
                                    return@addOnCompleteListener
                                }

                                val firebaseUid = result.user!!.uid

                                // If user does not exist in Firebase Authentication DB
                                if (result.additionalUserInfo?.isNewUser == true) {
                                    onSuccessNewUser.invoke(firebaseUid)
                                } else {
                                    FirestoreUtil.getUserFromUid(firebaseUid)
                                        .addOnSuccessListener { user ->
                                            // if user exists in Firebase Authentication DB, but not in Firestore DB
                                            if (user == null) {
                                                onSuccessNewUser.invoke(firebaseUid)
                                            } else {
                                                onSuccessExistingUser.invoke()
                                            }
                                        }
                                        .addOnFailureListener { e ->
                                            Timber.e("Failure getting user $firebaseUid - $e")
                                        }
                                }
                            } else {
                                Timber.e("Error signing in with email link : ${task.exception}")
                                when (task.exception) {
                                    is FirebaseAuthInvalidCredentialsException -> {
                                        Timber.e("Email is wrong")
                                        onFailWrongEmail.invoke()
                                    }
                                    is FirebaseAuthActionCodeException -> {
                                        Timber.e("Link has expired")
                                        onFailLinkExpired.invoke()
                                    }
                                    else -> {
                                        Timber.e("Unknown Firebase exception")
                                        onFailDefault.invoke()
                                    }
                                }
                            }
                        }
                } catch (e: Exception) {
                    Timber.e("Error while signing in with email link : $e")
                }
            }
        }

        fun signOut() {
            auth.signOut()
        }

        fun getCurrentUser(): FirebaseUser? {
            return auth.currentUser
        }
    }
}
