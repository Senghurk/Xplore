package com.bugdroiders.xploreapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.bugdroiders.xploreapp.databinding.ActivityLoginBinding
import com.bugdroiders.xploreapp.databinding.ActivityTravelerMainBinding
import com.bugdroiders.xploreapp.fragments.TravelerBookingsFragment
import com.bugdroiders.xploreapp.fragments.TravelerMainFragment
import com.bugdroiders.xploreapp.fragments.TravelerProfileFragment

class TravelerMainActivity : AppCompatActivity() {
    private val view: ActivityTravelerMainBinding by lazy { ActivityTravelerMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view.root)

        view.bnv.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.item_home -> changeFragment(TravelerMainFragment())
                R.id.item_profile -> changeFragment(TravelerProfileFragment())
                R.id.item_booking -> changeFragment(TravelerBookingsFragment())
                else -> false
            }
        }
    }

    private fun changeFragment(fragment: Fragment): Boolean {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fcView, fragment)
            .addToBackStack(fragment::class.java.name)
            .commit()

        return true
    }
}