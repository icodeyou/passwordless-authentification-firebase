package com.hobeez.sourcerise.ui.util

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.hobeez.sourcerise.R
import com.hobeez.sourcerise.util.extensions.hide
import com.hobeez.sourcerise.util.extensions.show
import org.jetbrains.anko.support.v4.find

@Suppress("UNCHECKED_CAST")
open class BaseLceFragment<T : View> : BaseFragment() {

    lateinit var contentView: T
    lateinit var loadingView: View
    lateinit var errorView: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        contentView = find<View>(R.id.contentView) as T
        loadingView = find(R.id.loadingView)
        errorView = find(R.id.errorView)
    }

    fun showContent() {
        errorView.hide()
        loadingView.hide()
        contentView.show()
    }

    fun showLoading() {
        contentView.hide()
        errorView.hide()
        loadingView.show()
    }

    fun showError(throwable: Throwable?) {
        contentView.hide()
        loadingView.hide()
        errorView.text = getErrorMessage(throwable)
        errorView.show()
    }
}
