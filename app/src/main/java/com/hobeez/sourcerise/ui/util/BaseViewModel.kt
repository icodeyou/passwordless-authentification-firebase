package com.hobeez.sourcerise.ui.util

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel : ViewModel() {
    protected var disposables = CompositeDisposable()

    /**
     * @nodoc
     */
    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}
