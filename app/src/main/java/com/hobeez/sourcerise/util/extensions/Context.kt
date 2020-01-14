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

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.ContextCompat

private val DEFAULT_SHARED_PREFERENCES_FILE = "DEFAULT_SHARED_PREFERENCES_FILE"

fun Context.color(id: Int): Int = ContextCompat.getColor(this, id)

fun Context.sharedPreferences(function: (SharedPreferences.Editor.() -> Unit)) {
    val sharedPreferencesEditor =
            getSharedPreferences(DEFAULT_SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE).edit()
    sharedPreferencesEditor.function()
    sharedPreferencesEditor.apply()
    sharedPreferencesEditor.commit()
}

fun <T> Context.getFromSharedPreferences(key: String, default: T): T where T : Any {
    val sharedPreferences =
            getSharedPreferences(DEFAULT_SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE)
    val value = when (default) {
        is Int -> sharedPreferences.getInt(key, default)
        is String -> sharedPreferences.getString(key, default)
        is Boolean -> sharedPreferences.getBoolean(key, default)
        is Float -> sharedPreferences.getFloat(key, default)
        is Long -> sharedPreferences.getLong(key, default)
        else -> default
    }
    return value as T
}
