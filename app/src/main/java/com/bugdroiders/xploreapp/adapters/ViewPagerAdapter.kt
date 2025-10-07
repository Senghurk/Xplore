package com.bugdroiders.xploreapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bugdroiders.xploreapp.fragments.GuidesListFragment
import com.bugdroiders.xploreapp.fragments.ToursListFragment


private const val NUM_TABS = 2
class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment){
    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            1 -> return GuidesListFragment()
        }
        return ToursListFragment()
    }

}