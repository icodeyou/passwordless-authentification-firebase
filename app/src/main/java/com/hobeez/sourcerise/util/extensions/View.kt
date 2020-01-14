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

import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar

fun View.snackBar(messageId: Int) {
    Snackbar.make(this, messageId, Snackbar.LENGTH_LONG).show()
}

fun View.show() {
    animate()
            .setDuration(
                    context.resources.getInteger(android.R.integer.config_shortAnimTime).toLong())
            .alpha(1f)
            .withStartAction {
                alpha = 0f
                visibility = View.VISIBLE
            }
            .start()
}

fun View.hide() {
    animate()
            .setDuration(
                    context.resources.getInteger(android.R.integer.config_shortAnimTime).toLong())
            .alpha(0f)
            .withEndAction { visibility = View.INVISIBLE }
            .start()
}

fun EditText.onImeActionDone(function: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        when (actionId) {
            EditorInfo.IME_ACTION_DONE -> {
                function()
                true
            }
            else -> false
        }
    }
}
