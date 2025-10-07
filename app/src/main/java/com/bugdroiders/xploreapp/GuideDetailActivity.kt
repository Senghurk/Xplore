package com.bugdroiders.xploreapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bugdroiders.xploreapp.databinding.ActivityGuideDetailBinding
import com.bugdroiders.xploreapp.databinding.ActivityGuideTourDetailBinding

class GuideDetailActivity : AppCompatActivity() {
    private val view: ActivityGuideDetailBinding by lazy { ActivityGuideDetailBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view.root)
    }
}