package com.hobeez.sourcerise.ui.catalog

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.hobeez.sourcerise.R
import com.hobeez.sourcerise.ui.util.BaseActivity
import kotlinx.android.synthetic.main.activity_viewpager.*

class ViewPagerActivity : BaseActivity() {

    // live data to make sure it gets restored with activity configuration change (rotation...)
    private var currentNavController: LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_viewpager)

        view_pager.adapter = ExamplePagerAdapter(this, supportFragmentManager)
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }
}
