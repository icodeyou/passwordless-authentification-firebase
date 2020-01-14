package com.hobeez.sourcerise.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.hobeez.sourcerise.R
import com.hobeez.sourcerise.ui.catalog.ViewPagerActivity
import com.hobeez.sourcerise.ui.login.LoginActivity
import com.hobeez.sourcerise.ui.util.BaseActivity
import com.hobeez.sourcerise.util.firebase.AuthUtil
import com.hobeez.sourcerise.util.network.Loading
import com.hobeez.sourcerise.util.network.Success
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent
import timber.log.Timber

class MainActivity : BaseActivity(), KoinComponent {

    // live data to make sure it gets restored with activity configuration change (rotation...)
    private var currentNavController: LiveData<NavController>? = null
    private val exampleViewModel: ExampleViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        updateLoginLogoutButtonsVisibilities()

        btViewpager.setOnClickListener {
            startActivity<ViewPagerActivity>()
        }

        btLogin.setOnClickListener {
            startActivity<LoginActivity>()
        }

        btSignOut.setOnClickListener {
            AuthUtil.signOut()
            updateLoginLogoutButtonsVisibilities()
        }

        exampleViewModel.loadKittens()
        exampleViewModel.kittenList.observe(this, Observer { resource ->
            when (resource) {
                is Success<List<String>> -> showData(resource.data)
                is Loading -> showLoading()
                is Error -> showError(resource.error)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        updateLoginLogoutButtonsVisibilities()
    }

    private fun updateLoginLogoutButtonsVisibilities() {
        btLogin.visibility = if (AuthUtil.getCurrentUser() == null) View.VISIBLE else View.GONE
        btSignOut.visibility = if (AuthUtil.getCurrentUser() != null) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    private fun showError(error: Throwable?) {
        Timber.e("error while loading user : " + error.toString())
    }

    private fun showData(data: List<String>?) {
        Timber.i("Result : ")
        if (data != null) {
            for (item in data) {
                Timber.i(item)
            }
        } else Timber.i("Null")
        // toast(String.format("%d kittens loaded (see log)", data?.size))
    }

    private fun showLoading() {
        Timber.d("loading kittens...")
        // toast("DEV - Loading kittens...")
    }
}
