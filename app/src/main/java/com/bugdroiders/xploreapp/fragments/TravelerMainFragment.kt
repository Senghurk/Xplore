package com.bugdroiders.xploreapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.bugdroiders.xploreapp.R
import com.bugdroiders.xploreapp.adapters.ViewPagerAdapter
import com.bugdroiders.xploreapp.databinding.FragmentTravelerMainBinding
import com.bugdroiders.xploreapp.viewModels.TourViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator



class TravelerMainFragment : Fragment() {

    private lateinit var view: FragmentTravelerMainBinding
    private lateinit var viewAdapter: ViewPagerAdapter
    private lateinit var viewPager: ViewPager2

    private val tabNameArray = arrayOf(
        "Tours",
        "Guides"
    )

    private val viewModel: TourViewModel by activityViewModels<TourViewModel>()

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?, savedInstanceState: Bundle?): View {
        view = FragmentTravelerMainBinding.inflate(inflater, container, false)

        return view.root
    }

    override fun onViewCreated(view2: View, savedInstanceState: Bundle?) {
        val tabLayout = view2.findViewById<TabLayout>(R.id.tabLayout)

        viewAdapter = ViewPagerAdapter(this)
        viewPager = view2.findViewById(R.id.viewPager)

        viewPager.adapter = viewAdapter
        TabLayoutMediator(tabLayout, viewPager){tab, position ->
            tab.text = tabNameArray[position]
        }.attach()

        view.viewPager.registerOnPageChangeCallback(
            object: ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    viewModel.setPosition(position)
                }
            }
        )

        view.searchView.setOnQueryTextListener (
            object : OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
//                    if (query != null) {
//                        Log.d("TOUR", query)
//                    }

                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText.isNullOrEmpty()) {
                        viewModel.setQuery("")
                    }

                    if (newText != null) {
                        viewModel.setQuery(newText)
                    } else {
                        viewModel.setQuery("")
                    }

                    return true
                }
            }
        )
    }
}