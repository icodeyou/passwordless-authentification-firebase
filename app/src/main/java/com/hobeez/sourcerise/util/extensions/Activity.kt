/*
 * MIT License
 *
 * Copyright (c) 2019 Appstud
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.hobeez.sourcerise.util.extensions

import android.app.Activity
import android.view.inputmethod.InputMethodManager

inline fun androidx.fragment.app.FragmentManager.inTransaction(
    func: androidx.fragment.app.FragmentTransaction.() -> Unit
) {
    val fragmentTransaction = beginTransaction()
    fragmentTransaction.func()
    fragmentTransaction.commit()
}

fun androidx.fragment.app.FragmentActivity.addFragment(
    fragment: androidx.fragment.app.Fragment,
    frameId: Int,
    addBackStack: Boolean = false,
    tag: String? = null
) {
    supportFragmentManager.inTransaction {
        add(frameId, fragment)
        if (addBackStack) addToBackStack(tag)
    }
}

fun androidx.fragment.app.FragmentActivity.replaceFragment(
    fragment: androidx.fragment.app.Fragment,
    frameId: Int,
    addBackStack: Boolean = false,
    tag: String? = null
) {
    supportFragmentManager.inTransaction {
        replace(frameId, fragment)
        if (addBackStack) addToBackStack(tag)
    }
}

inline fun androidx.fragment.app.FragmentActivity.setContentFragment(
    containerViewId: Int,
    f: () -> androidx.fragment.app.Fragment
): androidx.fragment.app.Fragment? {
    val manager = supportFragmentManager
    val fragment = manager?.findFragmentById(containerViewId)
    fragment?.let { return it }
    return f().apply { manager?.beginTransaction()?.add(containerViewId, this)?.commit() }
}

fun Activity.hideKeyboard() {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    currentFocus?.let {
        inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
    }
}
