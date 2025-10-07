package com.bugdroiders.xploreapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bugdroiders.xploreapp.databinding.ActivityHomeBinding
import com.bugdroiders.xploreapp.databinding.ActivityLoginBinding

class HomeActivity : AppCompatActivity() {
    private val view: ActivityHomeBinding by lazy { ActivityHomeBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view.root)
    }
}