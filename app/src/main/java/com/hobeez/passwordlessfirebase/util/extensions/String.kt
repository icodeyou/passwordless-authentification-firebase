/*
 * MIT License
 *
 */

package com.hobeez.passwordlessfirebase.util.extensions

import android.util.Patterns

fun String.isValidPhoneNumber(): Boolean {
    return length == 12
}

fun String.isValidPhoneCode(): Boolean {
    return length == 6
}

fun String.isEmailValid(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}
