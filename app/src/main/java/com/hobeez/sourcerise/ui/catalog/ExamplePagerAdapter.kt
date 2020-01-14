package com.hobeez.sourcerise.ui.catalog

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ExamplePagerAdapter(
    val context: Context,
    mgr: FragmentManager
) :
    FragmentPagerAdapter(mgr, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        return CatalogFragment.newInstance(position)
    }
}
