package com.hobeez.passwordlessfirebase.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import com.hobeez.passwordlessfirebase.R
import com.hobeez.passwordlessfirebase.ui.util.BaseFragment

class LoginLoadingFragment : BaseFragment() {

    private val PADDING_PROGRESSBAR = 300
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val progressBar = ProgressBar(activity)
        progressBar.indeterminateDrawable.setColorFilter(ContextCompat.getColor(context!!, R.color.white),android.graphics.PorterDuff.Mode.MULTIPLY)
        progressBar.setPadding(PADDING_PROGRESSBAR, PADDING_PROGRESSBAR, PADDING_PROGRESSBAR, PADDING_PROGRESSBAR)
        return progressBar
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorPrimaryDark))
    }
}
