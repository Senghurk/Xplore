package com.bugdroiders.xploreapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.viewModels
import com.bugdroiders.xploreapp.data.caches.UserCache
import com.bugdroiders.xploreapp.databinding.ActivityGuideCreateProfileBinding
import com.bugdroiders.xploreapp.models.Guide
import com.bugdroiders.xploreapp.viewModels.GuideCreateProfileViewModel

class GuideCreateProfileActivity : AppCompatActivity() {
    private val view : ActivityGuideCreateProfileBinding by lazy { ActivityGuideCreateProfileBinding.inflate(layoutInflater) }
    private val viewModel: GuideCreateProfileViewModel by viewModels<GuideCreateProfileViewModel>()
    private val languages = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view.root)

        val cache = UserCache(this)
        val userInfo = cache.getUserInfo()


        setSupportActionBar(view.mtbGuideProfile)

        supportActionBar?.apply {
            title = "Create your profile"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowCustomEnabled(true)
        }

        if (userInfo != null) {
            Log.d("ProfileExists", "It's checking if exists.")
            userInfo.email?.let { viewModel.checkIfProfileExists(it) }
        }

        view.btnCreateProfile.setOnClickListener {
            val introduction = view.etIntroduction.text.toString()
            val shortDescription = view.etShortDescription.text.toString()
            val location = view.etLocation.text.toString()

            val guide = Guide(
                name= userInfo?.fullName,
                image = userInfo?.imageUrl,
                introduction= introduction,
                shortDescription = shortDescription,
                email = userInfo?.email,
                location = location,
                languages = languages,
                rating = 0f,
                reviewCount = 0
            )
            viewModel.createProfile(guide)
        }

        findViewById<CheckBox>(R.id.checkbox_english)
            .setOnCheckedChangeListener {_, isChecked ->
                Log.d("CHECKBOXES", "English is checked $isChecked")
                if (isChecked) {
                    languages.add("English")
                } else {
                    languages.remove("English")
                }
                Log.d("CHECKBOXES", languages.toString())
            }

        findViewById<CheckBox>(R.id.checkbox_japanese)
            .setOnCheckedChangeListener {_, isChecked ->
                Log.d("CHECKBOXES", "Japanese is checked $isChecked")
                if (isChecked) {
                    languages.add("Japanese")
                } else {
                    languages.remove("Japanese")
                }
                Log.d("CHECKBOXES", languages.toString())
            }

        findViewById<CheckBox>(R.id.checkbox_thai)
            .setOnCheckedChangeListener {_, isChecked ->
                Log.d("CHECKBOXES", "Thai is checked $isChecked")
                if (isChecked) {
                    languages.add("Thai")
                } else {
                    languages.remove("Thai")
                }
                Log.d("CHECKBOXES", languages.toString())
            }

        findViewById<CheckBox>(R.id.checkbox_chinese)
            .setOnCheckedChangeListener {_, isChecked ->
                Log.d("CHECKBOXES", "Thai is checked $isChecked")
                if (isChecked) {
                    languages.add("Chinese")
                } else {
                    languages.remove("Chinese")
                }
                Log.d("CHECKBOXES", languages.toString())
            }

        findViewById<CheckBox>(R.id.checkbox_spanish)
            .setOnCheckedChangeListener {_, isChecked ->
                Log.d("CHECKBOXES", "Thai is checked $isChecked")
                if (isChecked) {
                    languages.add("Spanish")
                } else {
                    languages.remove("Spanish")
                }
                Log.d("CHECKBOXES", languages.toString())
            }

        observeView()
    }

    private fun observeView() {
        viewModel.isProfileCreated.observe(this) {
            if (it) {
                Toast.makeText(this, "Your profile is created.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, GuideMainActivity::class.java)
                startActivity(intent)
            }
        }

        viewModel.isFound.observe(this) {
            Log.d("ProfileExists", viewModel.isFound.value.toString())
            view.btnCreateProfile.isEnabled = !it
        }

        viewModel.isLoading.observe(this) {
            if (it) {
                view.pbCreateProfile.visibility = View.VISIBLE
            } else {
                view.pbCreateProfile.visibility = View.GONE
            }

        }

        viewModel.guideInfo.observe(this) {
            if (it != null) {
                view.etIntroduction.setText(it.introduction.toString())
                view.etLocation.setText(it.location.toString())
                view.etShortDescription.setText(it.shortDescription.toString())
                if (it.languages?.contains("Chinese") == true) {
                    view.checkboxChinese.isChecked = true
                }

                if (it.languages?.contains("Japanese") == true) {
                    view.checkboxJapanese.isChecked = true
                }

                if (it.languages?.contains("Spanish") == true) {
                    view.checkboxSpanish.isChecked = true
                }

                if (it.languages?.contains("Thai") == true) {
                    view.checkboxThai.isChecked = true
                }

                if (it.languages?.contains("English") == true) {
                    view.checkboxEnglish.isChecked = true
                }
                view.etIntroduction.isEnabled = false
                view.etLocation.isEnabled = false
                view.etShortDescription.isEnabled = false
                view.checkboxChinese.isEnabled = false
                view.checkboxSpanish.isEnabled = false
                view.checkboxJapanese.isEnabled = false
                view.checkboxThai.isEnabled = false
                view.checkboxEnglish.isEnabled = false
            }
        }
    }

}