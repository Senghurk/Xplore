package com.bugdroiders.xploreapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.bugdroiders.xploreapp.databinding.ActivityGuideMainBinding
import com.bugdroiders.xploreapp.fragments.GuideBookingFragment
import com.bugdroiders.xploreapp.fragments.GuideToursFragment
import com.bugdroiders.xploreapp.fragments.TravelerMainFragment
import com.bugdroiders.xploreapp.fragments.TravelerProfileFragment
import com.bugdroiders.xploreapp.viewModels.GuideMainViewModel

class GuideMainActivity : AppCompatActivity() {
    private val view : ActivityGuideMainBinding by lazy { ActivityGuideMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view.root)

        view.guideBnv.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.item_booking -> changeFragment(GuideBookingFragment())
                R.id.item_my_tours -> changeFragment(GuideToursFragment())
                R.id.item_guide_profile -> changeFragment(TravelerProfileFragment())
                else -> false
            }
        }
    }

    private fun changeFragment(fragment: Fragment): Boolean {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.guideFcView, fragment)
            .addToBackStack(fragment::class.java.name)
            .commit()

        return true
    }
}