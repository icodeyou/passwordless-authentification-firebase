package com.hobeez.sourcerise.util.network

sealed class Resource<out T> constructor(val data: T?, val refresh: Boolean, val error: Throwable?)

class Success<out T>(data: T?, refresh: Boolean = false) : Resource<T>(data, refresh, null)
class Loading(refresh: Boolean = false) : Resource<Nothing>(null, refresh, null)
class Error(error: Throwable?) : Resource<Nothing>(null, false, error)
